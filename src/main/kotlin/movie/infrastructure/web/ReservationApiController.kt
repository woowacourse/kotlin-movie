package movie.infrastructure.web

import movie.domain.amount.Point
import movie.domain.payment.PaymentMethod
import movie.domain.payment.PriceCalculator
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.seat.SeatInputParser
import movie.infrastructure.web.dto.ReservationItemRequest
import movie.infrastructure.web.dto.ReservationItemResponse
import movie.infrastructure.web.dto.ReservationRequest
import movie.infrastructure.web.dto.ReservationResponse
import movie.repository.ReservationRepository
import movie.repository.ScreeningRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/reservations")
class ReservationApiController(
    private val screeningRepository: ScreeningRepository,
    private val reservationRepository: ReservationRepository,
    private val priceCalculator: PriceCalculator,
    private val seatInputParser: SeatInputParser = SeatInputParser(),
) {
    @PostMapping
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ResponseEntity<ReservationResponse> {
        val reservations = Reservations(request.reservations.map { createReservation(it) })
        val paymentMethod = PaymentMethod.from(request.paymentMethod)
        val paymentResult = priceCalculator.calculate(reservations, Point(request.usedPoints), paymentMethod)

        val reservationId =
            reservationRepository.save(
                reservations,
                paymentResult.totalPrice,
                paymentResult.usedPoint,
                paymentResult.paymentMethodName(),
            )

        val response =
            ReservationResponse(
                reservationId = reservationId,
                reservations = request.reservations.map { ReservationItemResponse(it.screeningId, it.seats) },
                usedPoints = paymentResult.usedPoint.value,
                paymentMethod = paymentResult.paymentMethodName(),
                totalPrice = paymentResult.totalPrice.value,
            )
        return ResponseEntity.created(URI.create("/api/reservations/$reservationId")).body(response)
    }

    private fun createReservation(item: ReservationItemRequest): Reservation {
        val screening = screeningRepository.findById(item.screeningId)
        val seatPositions = seatInputParser.parse(item.seats.joinToString(","))
        val selectedSeats = screening.toSelectedSeats(seatPositions)
        val availableSeats = screening.isReserveAvailable(selectedSeats)
        return screening.reserve(availableSeats).createReservation(availableSeats)
    }
}
