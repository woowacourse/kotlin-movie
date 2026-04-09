package model.discount

import model.Money
import model.screening.Screening
import java.time.LocalTime

object TimeDiscountPolicy : DiscountPolicy {
    private val DISCOUNT_AMOUNT = Money(2_000)
    private val MORNING_CUTOFF = LocalTime.of(11, 0)
    private val EVENING_CUTOFF = LocalTime.of(20, 0)

    override fun calculateDiscountAmount(
        price: Money,
        screening: Screening,
    ): Money {
        val startTime = screening.startShowTime
        if (startTime.isBefore(MORNING_CUTOFF) || !startTime.isBefore(EVENING_CUTOFF)) return DISCOUNT_AMOUNT
        return Money(0)
    }
}
