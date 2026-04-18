package model.payment.policy.discount

import model.payment.PayType
import model.payment.policy.price.PricingPolicy
import model.reservation.MovieReservationResult

class MovieDayDiscount : DiscountPolicy {
    override fun apply(
        price: Int,
        reservations: List<MovieReservationResult.Success>,
        payType: PayType,
    ): Int {
        val discount =
            reservations
                .filter { reservation -> MOVIE_DAYS.any { reservation.screenTime.start.isSameDayOfMonth(it) } }
                .sumOf {
                    val price = PricingPolicy.price(it.seat.grade)
                    (price * MOVIE_DAY_DISCOUNT_RATE).toInt()
                }
        return price - discount
    }

    companion object {
        private val MOVIE_DAYS = listOf(10, 20, 30)
        private const val MOVIE_DAY_DISCOUNT_RATE = 0.1
    }
}
