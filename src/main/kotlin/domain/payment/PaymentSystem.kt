package domain.payment

import domain.discount.EventDiscountPolicy
import domain.discount.PaymentDiscount
import domain.discount.PaymentDiscountPolicy
import domain.discount.TheaterEventDiscount
import domain.reservation.TicketBucket
import domain.seat.SeatGrade

class PaymentSystem(
    private val eventDiscountPolicy: EventDiscountPolicy = TheaterEventDiscount(),
    private val paymentDiscountPolicy: PaymentDiscountPolicy = PaymentDiscount(),
) {
    fun calculate(
        point: Point,
        payment: PaymentType,
        ticketBucket: TicketBucket,
    ): Money {
        var total = Money(0)
        ticketBucket.tickets.forEach { ticket ->
            ticket.seatPositions.positions.forEach { position ->
                total +=
                    eventDiscountPolicy.discount(
                        Money(position.price),
                        ticket.screening.startTime,
                    )
            }
        }
        total -= point.amount
        return paymentDiscountPolicy.discount(total, payment)
    }
}
