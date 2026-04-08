package domain.discount

import domain.amount.Money
import domain.screening.ScreeningDateTime

class DiscountPolicies(
    private val percentagePolicies: List<DiscountPolicy>,
    private val fixedPolicies: List<DiscountPolicy>
) {
    fun calculateDiscount(price: Money, dateTime: ScreeningDateTime): Money {
        var result = price
        percentagePolicies.forEach {
            result = it.calculateDiscount(result, dateTime)
        }
        fixedPolicies.forEach {
            result = it.calculateDiscount(result, dateTime)
        }
        return result
    }
}
