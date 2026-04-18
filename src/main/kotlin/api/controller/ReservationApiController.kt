package api.controller

import api.dto.ReservationItemDto
import api.dto.ReservationRequest
import api.dto.ReservationResponse
import domain.common.Money
import domain.payment.PaymentType
import domain.reservation.Reservation
import domain.seat.Column
import domain.seat.Row
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import domain.ticket.Ticket
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import repository.ReservationRepository
import repository.ScreeningRepository

@RestController
@RequestMapping("/api/reservations")
class ReservationApiController(
    private val screeningRepository: ScreeningRepository,
    private val reservationRepository: ReservationRepository
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun reserve(@RequestBody request: ReservationRequest): ReservationResponse {
        val tickets = request.reservations.map { item ->
            val screening = screeningRepository.findById(item.screeningId)
            val positions = item.seats.map { parseSeat(it) }
            val seatPositions = SeatPositions(positions)
            screening.isReservable(seatPositions)
            Ticket(screening, seatPositions)
        }

        val paymentSystem = domain.payment.PaymentSystem(
            ticketDiscountStrategy = domain.discount.TicketDiscountPolicy(
                listOf(domain.discount.MoviedayDiscount(), domain.discount.TimeDiscount())
            ),
            totalDiscountStrategy = domain.discount.TotalDiscountPolicy(
                listOf(domain.discount.PaymentDiscount())
            )
        )

        val finalPrice = paymentSystem.calculate(
            point = domain.common.Point(request.usedPoints),
            payment = PaymentType.valueOf(request.paymentMethod),
            ticketBucket = domain.ticket.TicketBucket(tickets)
        )

        val reservation = Reservation(
            tickets = tickets,
            usedPoints = Money(request.usedPoints),
            paymentMethod = PaymentType.valueOf(request.paymentMethod),
            totalPrice = finalPrice
        )

        val savedId = reservationRepository.save(reservation)

        return ReservationResponse(
            reservationId = savedId,
            reservations = request.reservations,
            usedPoints = request.usedPoints,
            paymentMethod = request.paymentMethod,
            totalPrice = finalPrice.amount
        )
    }

    private fun parseSeat(seatStr: String): SeatPosition {
        val rowStr = seatStr.take(1)
        val colInt = seatStr.substring(1).toInt()
        return SeatPosition(Row(rowStr), Column(colInt))
    }
}
