package model.payment

import java.time.LocalTime

object DiscountPolicy {
    val MOVIE_DAYS = listOf(10, 20, 30)
    const val MOVIE_DAY_DISCOUNT_RATE = 0.1
    const val EARLY_AND_LATE_DISCOUNT_AMOUNT = 2_000

    const val CREDIT_CARD_DISCOUNT_RATE = 0.05
    const val CASH_DISCOUNT_RATE = 0.02
    val EARLY_DISCOUNT_END: LocalTime = LocalTime.of(11, 0)
    val LATE_DISCOUNT_START: LocalTime = LocalTime.of(20, 0)
}