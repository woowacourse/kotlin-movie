package model.payment

import model.payment.policy.discount.DiscountPolicy
import model.payment.policy.price.PricingPolicy
import model.reservation.MovieReservationResult

class MoviePayment(
    private val reservations: List<MovieReservationResult.Success>,
    private val policies: List<DiscountPolicy>,
) {
    val originalPrice: Int = reservations.sumOf { PricingPolicy.price(it.seat.grade) }

    fun getFinalPrice(payType: PayType): Int =
        policies.fold(originalPrice) { acc, policy ->
            policy.apply(acc, reservations, payType)
        }
}
