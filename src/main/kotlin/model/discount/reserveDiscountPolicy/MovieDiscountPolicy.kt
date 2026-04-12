package model.discount.reserveDiscountPolicy

import java.time.LocalDateTime

class MovieDiscountPolicy(
    private val movieDiscountPolicies: List<ReserveDiscountPolicy>,
) : ReserveDiscountPolicy {
    override fun calculatePrice(
        price: Int,
        reservedDateTime: LocalDateTime,
    ): Int =
        movieDiscountPolicies.fold(price) { acc, policy ->
            policy.calculatePrice(acc, reservedDateTime)
        }
}
