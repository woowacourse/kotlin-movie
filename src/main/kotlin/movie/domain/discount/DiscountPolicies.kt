package movie.domain.discount

import movie.domain.amount.Money
import java.time.LocalDateTime

class DiscountPolicies(
    private val percentagePolicies: List<DiscountPolicy>,
    private val fixedPolicies: List<DiscountPolicy>,
) {
    fun applyDiscount(
        price: Money,
        dateTime: LocalDateTime,
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
