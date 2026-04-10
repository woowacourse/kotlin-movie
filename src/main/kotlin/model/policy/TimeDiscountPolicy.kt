package model.policy

import model.Money
import model.screening.Screening
import java.time.LocalTime

object TimeDiscountPolicy : DiscountPolicy {
    private val DISCOUNT_AMOUNT = Money(2_000)
    private val MORNING_CUTOFF = LocalTime.of(11, 0)
    private val EVENING_CUTOFF = LocalTime.of(20, 0)

    override fun getDiscountEffect(screening: Screening): DiscountEffect {
        val startTime = screening.startShowTime
        if (startTime <= MORNING_CUTOFF || startTime >= EVENING_CUTOFF) {
            return AmountDiscountEffect(
                DISCOUNT_AMOUNT,
            )
        }
        return NoDiscountEffect
    }
}
