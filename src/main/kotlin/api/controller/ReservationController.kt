package api.controller

import api.dto.ReservationItemResponse
import api.dto.ReservationRequest
import api.dto.ReservationResponse
import database.repository.MovieScreeningRepository
import database.repository.ReservationRepository
import model.payment.MoviePayment
import model.payment.PayType
import model.payment.policy.discount.EarlyLateDiscount
import model.payment.policy.discount.MovieDayDiscount
import model.payment.policy.discount.PayTypeDiscount
import model.payment.policy.discount.PointDiscount
import model.reservation.MovieReservationResult
import model.seat.SeatColumn
import model.seat.SeatRow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservationController {
    private val reservationRepository = ReservationRepository()
    private val movieScreeningRepository = MovieScreeningRepository()

    @PostMapping("/api/reservations")
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ResponseEntity<ReservationResponse> {
        val payType = PayType.valueOf(request.paymentMethod)

        val reservationId = reservationRepository.createReservation()
        val allResults = mutableListOf<MovieReservationResult.Success>()
        request.reservations.forEach { item ->
            val screening =
                movieScreeningRepository.findByScreeningId(item.screeningId)
                    ?: run {
                        reservationRepository.delete(reservationId)
                        return ResponseEntity.badRequest().build()
                    }

            item.seats.forEach { seat ->
                val row = SeatRow(seat.substring(0, 1))
                val col = SeatColumn(seat.substring(1).toInt())
                val result = screening.reserve(row, col)
                when (result) {
                    is MovieReservationResult.Success -> {
                        reservationRepository.saveSeat(reservationId, item.screeningId, result.seat)
                        allResults += result
                    }
                    is MovieReservationResult.Failed -> {
                        reservationRepository.delete(reservationId)
                        return ResponseEntity.badRequest().build()
                    }
                }
            }
        }

        val moviePayment =
            MoviePayment(
                reservations = allResults,
                policies =
                    listOf(
                        MovieDayDiscount(),
                        EarlyLateDiscount(),
                        PointDiscount(request.usedPoints),
                        PayTypeDiscount(),
                    ),
            )
        val totalPrice = moviePayment.getFinalPrice(payType)
        reservationRepository.updatePayment(reservationId, totalPrice, request.usedPoints, payType)

        val response =
            ReservationResponse(
                reservationId = reservationId,
                reservations =
                    request.reservations.map { item ->
                        ReservationItemResponse(
                            screeningId = item.screeningId,
                            seats = item.seats,
                        )
                    },
                usedPoints = request.usedPoints,
                paymentMethod = request.paymentMethod,
                totalPrice = totalPrice,
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
