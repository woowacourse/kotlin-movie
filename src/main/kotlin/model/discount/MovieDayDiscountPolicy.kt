package model.discount

import model.Money
import model.screening.Screening

object MovieDayDiscountPolicy : DiscountPolicy {
    private const val DISCOUNT_RATE = 0.1
    private val DISCOUNT_DAYS = listOf(10, 20, 30)

    override fun calculateDiscountAmount(
        price: Money,
        screening: Screening,
    ): Money {
        val day = screening.showDate.dayOfMonth
        if (!DISCOUNT_DAYS.contains(day)) return Money(0)
        return price * DISCOUNT_RATE
    }
}
