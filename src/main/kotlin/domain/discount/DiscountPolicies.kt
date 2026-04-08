package domain.discount

import domain.amount.Money
import domain.screening.ScreeningDateTime

class DiscountPolicies(
    private val percentagePolicies: List<DiscountPolicy>,
    private val fixedPolicies: List<DiscountPolicy>,
) {
    fun applyDiscount(
        price: Money,
        dateTime: ScreeningDateTime,
    ): Money {
        var result = price
        percentagePolicies.forEach {
            result = it.applyDiscount(result, dateTime)
        }
        fixedPolicies.forEach {
            result = it.applyDiscount(result, dateTime)
        }
        return result
    }
}
