package domain.payment

import domain.Money

object Cash : PaymentMethod {
    private const val DISCOUNT_RATE = 0.02
    override fun calculateDiscountAmount(price: Money): Money = price * DISCOUNT_RATE
}
