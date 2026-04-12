package movie.domain.discount

import movie.domain.amount.Price
import java.time.LocalDateTime

class DiscountPolicies(
    private val percentagePolicies: List<PercentageDiscountPolicy>,
    private val fixedPolicies: List<FixedAmountDiscountPolicy>,
) {
    fun applyDiscount(
        price: Price,
        dateTime: LocalDateTime,
    ): Price {
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
