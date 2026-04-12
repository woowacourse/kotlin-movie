package model.discount.payDiscountPolicy

class PayDiscountBenefits(
    private val payDiscountPolicies: List<PayDiscountPolicy>,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Int): Int =
        payDiscountPolicies.fold(price) { acc, policy ->
            policy.calculatePrice(acc)
        }
}
