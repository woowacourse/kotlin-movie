package model.payment

import model.MovieReservationResult
import model.payment.DiscountPolicy.EARLY_AND_LATE_DISCOUNT_AMOUNT
import model.payment.DiscountPolicy.EARLY_DISCOUNT_END
import model.payment.DiscountPolicy.LATE_DISCOUNT_START
import model.payment.DiscountPolicy.MOVIE_DAYS
import model.payment.DiscountPolicy.MOVIE_DAY_DISCOUNT_RATE

class MoviePayment(
    val reservations: List<MovieReservationResult.Success>,
) {
    val originalPrice: Int = reservations.sumOf { it.seat.price }

    fun getFinalPrice(
        payType: PayType,
        point: Int,
    ): Int =
        originalPrice
            .let(::applyMovieDayDiscount)
            .let(::applyEarlyAndLateDiscount)
            .let { applyPoint(it, point) }
            .let { applyPayTypeDiscount(it, payType) }

    private fun applyPayTypeDiscount(
        price: Int,
        payType: PayType,
    ): Int = price - (price * payType.discountRate).toInt()

    private fun applyPoint(
        price: Int,
        point: Int,
    ): Int = price - point

    private fun applyMovieDayDiscount(price: Int): Int {
        val discount =
            reservations
                .filter { reservation -> MOVIE_DAYS.any { reservation.screenTime.start.isSameDayOfMonth(it) } }
                .sumOf { (it.seat.price * MOVIE_DAY_DISCOUNT_RATE).toInt() }
        return price - discount
    }

    private fun applyEarlyAndLateDiscount(price: Int): Int {
        val applyCount =
            reservations.count { reservation ->
                val startTime = reservation.screenTime.start
                startTime.isTimeOfDayAtOrBefore(EARLY_DISCOUNT_END) ||
                    startTime.isTimeOfDayAtOrAfter(LATE_DISCOUNT_START)
            }
        return price - applyCount * EARLY_AND_LATE_DISCOUNT_AMOUNT
    }
}
