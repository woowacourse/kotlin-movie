package model.payment

import model.MovieReservationResult
import model.payment.DiscountPolicy.CASH_DISCOUNT_RATE
import model.payment.DiscountPolicy.CREDIT_CARD_DISCOUNT_RATE
import model.payment.DiscountPolicy.EARLY_AND_LATE_DISCOUNT_AMOUNT
import model.payment.DiscountPolicy.EARLY_DISCOUNT_END
import model.payment.DiscountPolicy.LATE_DISCOUNT_START
import model.payment.DiscountPolicy.MOVIE_DAYS
import model.payment.DiscountPolicy.MOVIE_DAY_DISCOUNT_RATE

class MoviePayment(
    val reservations: List<MovieReservationResult.Success>,
    val calculateTotalPrice: Int = reservations.sumOf { it.seat.price },
    var currentPrice: Int = calculateTotalPrice,
) {
    init {
        if (currentPrice < 0) currentPrice = 0
    }

    fun applyDiscount() {
        applyMovieDayDiscount()
        applyEarlyAndLateDiscount()
    }

    fun pay(payType: PayType): Int =
        when (payType) {
            PayType.CREDIT_CARD -> {
                currentPrice -= (currentPrice * CREDIT_CARD_DISCOUNT_RATE).toInt()
                currentPrice
            }

            PayType.CASH -> {
                currentPrice -= (currentPrice * CASH_DISCOUNT_RATE).toInt()
                currentPrice
            }
        }

    fun applyPoint(point: Int) {
        currentPrice -= point
    }

    private fun applyMovieDayDiscount() {
        reservations.forEach { reservation ->
            for (day in MOVIE_DAYS) {
                if (reservation.screenTime.start.isSameDayOfMonth(day)) {
                    currentPrice -= (reservation.seat.price * MOVIE_DAY_DISCOUNT_RATE).toInt()
                }
            }
        }
    }

    private fun applyEarlyAndLateDiscount() {
        reservations.forEach { reservation ->
            val time = reservation.screenTime.start
            if (time.isTimeOfDayAtOrBefore(EARLY_DISCOUNT_END) ||
                time.isTimeOfDayAtOrAfter(LATE_DISCOUNT_START)
            ) {
                currentPrice -= EARLY_AND_LATE_DISCOUNT_AMOUNT
            }
        }
    }
}
