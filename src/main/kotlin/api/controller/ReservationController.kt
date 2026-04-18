package api.controller

import api.dto.CreateReservationRequest
import api.dto.CreateReservationResponse
import model.Point
import model.discount.MovieDayDiscountPolicy
import model.discount.TimeDiscountPolicy
import model.payment.Card
import model.payment.Cash
import model.payment.PaymentMethod
import model.payment.PaymentSystem
import model.reservation.Reservation
import model.reservation.Reservations
import model.seat.SeatNumber
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import repository.ReservationRepository
import repository.ScreeningRepository

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val screeningRepository: ScreeningRepository,
    private val reservationRepository: ReservationRepository,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createReservation(
        @RequestBody request: CreateReservationRequest,
    ): CreateReservationResponse {
        val reservations = buildReservations(request)
        val paymentMethod = request.paymentMethod.toPaymentMethod()
        val payResult =
            PaymentSystem(
                listOf(MovieDayDiscountPolicy, TimeDiscountPolicy),
                paymentMethod,
            ).pay(reservations, Point(request.usedPoints))
        val reservationId = reservationRepository.save(reservations)
        return CreateReservationResponse(
            reservationId = reservationId,
            reservations = request.reservations,
            usedPoints = payResult.usedPoint.value,
            paymentMethod = request.paymentMethod,
            totalPrice = payResult.finalPrice.value,
        )
    }

    private fun buildReservations(request: CreateReservationRequest): Reservations {
        var reservations = Reservations()
        for (item in request.reservations) {
            val screening =
                screeningRepository.findById(item.screeningId)
                    ?: throw IllegalArgumentException("존재하지 않는 상영입니다: ${item.screeningId}")
            val seats = screening.reserve(item.seats.map { SeatNumber(it[0], it.substring(1).toInt()) })
            reservations = reservations.addReservation(Reservation(screening, seats))
        }
        return reservations
    }

    private fun String.toPaymentMethod(): PaymentMethod =
        when (this) {
            "CREDIT_CARD" -> Card
            "CASH" -> Cash
            else -> throw IllegalArgumentException("지원하지 않는 결제 수단입니다: $this")
        }
}
