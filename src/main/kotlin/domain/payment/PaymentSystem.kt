package domain.payment

import domain.common.Money
import domain.discount.DiscountContext
import domain.discount.DiscountPolicy
import domain.discount.MovieDayDiscount
import domain.discount.PaymentDiscount
import domain.discount.TimeDiscount
import domain.ticket.TicketBucket

class PaymentSystem(
    private val discountPolicy: DiscountPolicy = DiscountPolicy(
        strategies = listOf(
            MovieDayDiscount(),
            TimeDiscount(),
            PaymentDiscount(),
        )
    ),
) {
    fun calculate(
        point: Point,
        payment: PaymentType,
        ticketBucket: TicketBucket,
    ): Money {
        var total = Money(0)

        ticketBucket.tickets.forEach { ticket ->
            total += discountPolicy.calculateDiscountResult(ticket.totalPrice, point, context = DiscountContext(
                dateTime = ticket.screening.startTime,
                paymentType = payment
            ) )
        }
        return total
    }
}
