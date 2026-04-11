package model.policy

import model.Money

sealed interface Discount

data class AmountDiscount(
    val amount: Money,
) : Discount

data class RateDiscount(
    val rate: Double,
) : Discount {
    init { require(rate in 0.0..1.0) { "할인율은 0.0과 1.0 사이어야 합니다." } }
}

data object NoDiscount : Discount
