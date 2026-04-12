package domain.discount

import domain.common.Money

class PaymentDiscount : DiscountStrategy {
    override fun apply(money: Money, context: DiscountContext): Money {
        val paymentType = context.paymentType
        return money * paymentType.discountRate
    }
}
