package model.policy

import model.screening.Screening

object MovieDayDiscountPolicy : DiscountPolicy {
    private const val DISCOUNT_PERCENT = 10
    private val DISCOUNT_DAYS = listOf(10, 20, 30)

    override val priority: Int
        get() = 1

    override fun findDiscount(screening: Screening): Discount {
        val day = screening.showDate.dayOfMonth
        if (DISCOUNT_DAYS.contains(day)) return PercentDiscount(DISCOUNT_PERCENT)
        return NoDiscount
    }
}
