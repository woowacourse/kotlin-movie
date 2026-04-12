package domain.discount

import domain.common.Money

class TicketDiscountPolicy(private val strategies: List<TicketDiscountStrategy>) {
    fun calculateDiscountResult(money: Money, context: TicketDiscountContext): Money {
        var result = money
        for (strategy in strategies) {
            result -= strategy.apply(result, context)
        }
        return result
    }
}

class TotalDiscountPolicy(private val strategies: List<TotalDiscountStrategy>) {
    fun calculateDiscountResult(money: Money, context: PaymentDiscountContext): Money {
        var result = money
        for (strategy in strategies) {
            result -= strategy.apply(result, context)
        }
        return result
    }
}
