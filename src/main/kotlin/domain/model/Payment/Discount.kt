package domain.model.Payment

import domain.model.screen.Screening
import java.time.LocalTime

data class Discount(
    private val screening: Screening? = null,
) {
    // 적용 순서: 무비데이(비율) -> 시간할인(정액)
    fun discountAmountApply(discountAmount: Int): Int =
        discountAmount
            .minus(discountAmount * movieDayDiscountPercent() / 100)
            .minus(timeDiscountAmount())
            .coerceAtLeast(0)

    private fun movieDayDiscountPercent(): Int =
        screening?.screeningDate?.dayOfMonth.let { dayOfMonth -> (dayOfMonth in MOVIE_DAYS).toDiscount(PERCENT_10) }

    private fun timeDiscountAmount(): Int {
        val source = screening ?: return 0
        return (source.startTime !in MORNING_CUTOFF..NIGHT_CUTOFF)
            .toDiscount(AMOUNT_2000)
    }

    private fun Boolean.toDiscount(value: Int): Int =
        takeIf { this }?.let { value } ?: 0

    private companion object {
        val MOVIE_DAYS = setOf(10, 20, 30)
        val MORNING_CUTOFF: LocalTime = LocalTime.of(11, 0)
        val NIGHT_CUTOFF: LocalTime = LocalTime.of(20, 0)
        const val PERCENT_10 = 10
        const val AMOUNT_2000 = 2_000
    }
}
