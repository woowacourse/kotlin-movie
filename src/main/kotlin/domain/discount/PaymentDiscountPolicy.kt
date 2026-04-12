package domain.discount

import domain.Money
import domain.PaymentType

interface PaymentDiscountPolicy {
    fun discount(
        money: Money,
        type: PaymentType,
    ): Money
}

class PaymentDiscount : PaymentDiscountPolicy {
    override fun discount(
        money: Money,
        type: PaymentType,
    ): Money = money * (1 - type.discountRate)
}
