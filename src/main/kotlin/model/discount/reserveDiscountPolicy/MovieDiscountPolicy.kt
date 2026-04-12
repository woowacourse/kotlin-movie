package model.discount.reserveDiscountPolicy

import model.seat.Price
import java.time.LocalDateTime

class MovieDiscountPolicy(
    private val movieDiscountPolicies: List<ReserveDiscountPolicy>,
) : ReserveDiscountPolicy {
    override fun calculatePrice(
        price: Price,
        reservedDateTime: LocalDateTime,
    ): Price =
        movieDiscountPolicies.fold(price) { acc, policy ->
            policy.calculatePrice(acc, reservedDateTime)
        }
}
