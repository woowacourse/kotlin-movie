package domain.purchase

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

object DiscountPolicy {
    private val movieDay = listOf<Int>(10, 20, 30)
    private val noneDiscountTimeBoundary =
        LocalTime(11, 0) to LocalTime(20, 0)

    const val MOVIE_DAY_DISCOUNT_PERCENT = 0.1
    const val NONE_DISCOUNT_PERCENT = 0.0

    const val TIME_DISCOUNT_PRICE = 2000
    const val NONE_DISCOUNT_PRICE = 0

    const val CARD_DISCOUNT_PERCENT = 0.05
    const val CASH_DISCOUNT_PERCENT = 0.02
    fun movieDayDiscount(date: LocalDateTime): Double {
        if (movieDay.contains(date.day)) {
            return MOVIE_DAY_DISCOUNT_PERCENT
        }
        return NONE_DISCOUNT_PERCENT
    }

    fun showTimeDiscount(date: LocalDateTime): Int {
        if (date.time > noneDiscountTimeBoundary.first && date.time < noneDiscountTimeBoundary.second) {
            return NONE_DISCOUNT_PRICE
        }
        return TIME_DISCOUNT_PRICE
    }

    fun paymentDiscount(method: PaymentMethod): Double {
        return when (method) {
            PaymentMethod.CARD -> CARD_DISCOUNT_PERCENT
            PaymentMethod.CASH -> CASH_DISCOUNT_PERCENT
        }
    }
}
