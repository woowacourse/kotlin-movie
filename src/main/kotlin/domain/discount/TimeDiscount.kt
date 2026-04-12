package domain.discount

import domain.common.Money

class TimeDiscount : TicketDiscountStrategy {
    override fun apply(
        money: Money,
        context: TicketDiscountContext
    ): Money {
        val hour = context.dateTime.hour
        if (hour !in 12..<20) return Money(2000)

        return Money(0)
    }
}
