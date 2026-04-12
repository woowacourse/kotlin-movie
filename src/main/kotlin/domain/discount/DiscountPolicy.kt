package domain.discount

import domain.common.Money
import domain.payment.Point

class DiscountPolicy(
    private val strategies: List<DiscountStrategy>,
) {
    fun calculateDiscountResult(
        money: Money,
        point: Point,
        context: DiscountContext
    ): Money {
        var result = money

        for (strategy in strategies) {
            val discountResult = strategy.apply(result, context)
            result -= discountResult
        }
        result -= point.amount
        return result
    }
}
