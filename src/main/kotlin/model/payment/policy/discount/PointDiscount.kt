package model.payment.policy.discount

import model.payment.PayType
import model.reservation.MovieReservationResult

class PointDiscount(
    private val point: Int,
) : DiscountPolicy {
    override fun apply(
        price: Int,
        reservations: List<MovieReservationResult.Success>,
        payType: PayType,
    ): Int = price - point
}
