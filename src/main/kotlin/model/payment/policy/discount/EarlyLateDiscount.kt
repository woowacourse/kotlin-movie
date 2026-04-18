package model.payment.policy.discount

import model.payment.PayType
import model.reservation.MovieReservationResult
import java.time.LocalTime

class EarlyLateDiscount : DiscountPolicy {
    override fun apply(
        price: Int,
        reservations: List<MovieReservationResult.Success>,
        payType: PayType,
    ): Int {
        val count =
            reservations.count { reservation ->
                val startTime = reservation.screenTime.start
                startTime.isTimeOfDayAtOrBefore(EARLY_END) || startTime.isTimeOfDayAtOrAfter(LATE_START)
            }

        return price - count * DISCOUNT_AMOUNT
    }

    companion object {
        private val EARLY_END: LocalTime = LocalTime.of(11, 0)
        private val LATE_START: LocalTime = LocalTime.of(20, 0)
        private const val DISCOUNT_AMOUNT = 2_000
    }
}
