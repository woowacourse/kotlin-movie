package domain.paycalculator.items

import domain.discountpolicy.PayMethod
import domain.discountpolicy.PayMethodDiscountPolicy
import domain.money.Money

class PayMethodDiscountCalculator(
    private val policies: Map<PayMethod, PayMethodDiscountPolicy>,
) {
    fun calculate(
        price: Money,
        payMethod: PayMethod,
    ): Money {
        val policy = policies[payMethod]
        return policy?.applyDiscount(price, payMethod) ?: price
    }
}
