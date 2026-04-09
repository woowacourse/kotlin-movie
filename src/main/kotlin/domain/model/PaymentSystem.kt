package domain.model

class PaymentSystem(
    private val discountPolicy: DiscountPolicy = DiscountPolicy(),
) {
    fun calculate(
        point: Point,
        payment: PaymentType,
        ticketBucket: TicketBucket,
    ): Money {
        var total = Money(0)

        ticketBucket.tickets.forEach { ticket ->
            total += discountPolicy.calculateDiscountResult(ticket.totalPrice, point, ticket.screening.startTime, payment)
        }
        return total
    }
}
