package model.policy

import model.screening.Screening

object MovieDayDiscountPolicy : DiscountPolicy {
    private const val DISCOUNT_RATE = 0.1
    private val DISCOUNT_DAYS = listOf(10, 20, 30)

    override fun getDiscountEffect(screening: Screening): DiscountEffect {
        val day = screening.showDate.dayOfMonth
        if (DISCOUNT_DAYS.contains(day)) return RateDiscountEffect(DISCOUNT_RATE)
        return NoDiscountEffect
    }
}
