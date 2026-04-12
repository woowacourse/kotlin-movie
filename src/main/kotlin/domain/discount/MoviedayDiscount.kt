package domain.discount

import domain.common.Money

class MovieDayDiscount : DiscountStrategy {
    override fun apply(money: Money, context: DiscountContext): Money {
        val date = context.dateTime.toLocalDate()
        if(date.dayOfMonth % 10 == 0) return Money((money.amount * 0.1).toInt())

        return Money(0)
    }
}
