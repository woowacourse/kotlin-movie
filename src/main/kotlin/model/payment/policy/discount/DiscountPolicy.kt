package model.payment.policy.discount

import model.payment.PayType
import model.reservation.MovieReservationResult

interface DiscountPolicy {
    fun apply(
        price: Int,
        reservations: List<MovieReservationResult.Success>,
        payType: PayType,
    ): Int
}
