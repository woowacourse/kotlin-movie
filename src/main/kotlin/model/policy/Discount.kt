package model.policy

import model.Money

sealed interface Discount {
    fun calculateDiscount(
        currentPrice: Money,
        seatCount: Int,
    ): Money
}

data class AmountDiscount(
    val amount: Money,
) : Discount {
    override fun calculateDiscount(
        currentPrice: Money,
        seatCount: Int,
    ): Money = amount * seatCount
}

data class PercentDiscount(
    val percent: Int,
) : Discount {
    init {
        require(percent in 0..100) { "할인율은 0과 100 사이의 정수여야 합니다." }
    }

    private val rate = percent.toDouble() / 100

    override fun calculateDiscount(
        currentPrice: Money,
        seatCount: Int,
    ): Money = currentPrice * rate
}

data object NoDiscount : Discount {
    override fun calculateDiscount(
        currentPrice: Money,
        seatCount: Int,
    ): Money = Money(0)
}
