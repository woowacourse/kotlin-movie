package model.payment.policy.discount

import model.payment.PayType
import model.reservation.MovieReservationResult

class PayTypeDiscount : DiscountPolicy {
    override fun apply(
        price: Int,
        reservations: List<MovieReservationResult.Success>,
        payType: PayType,
    ): Int = price - (price * payType.discountRate).toInt()
}
