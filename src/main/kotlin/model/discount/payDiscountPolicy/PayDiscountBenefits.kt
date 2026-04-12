package model.discount.payDiscountPolicy

import model.seat.Price

class PayDiscountBenefits(
    private val payDiscountPolicies: List<PayDiscountPolicy>,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Price): Price =
        payDiscountPolicies.fold(price) { acc, policy ->
            policy.calculatePrice(acc)
        }
}
