package domain.payment

import domain.Money

object Card: PaymentMethod {
    private const val DISCOUNT_RATE = 0.05
    override fun calculateDiscountAmount(price: Money): Money = price * DISCOUNT_RATE
}
