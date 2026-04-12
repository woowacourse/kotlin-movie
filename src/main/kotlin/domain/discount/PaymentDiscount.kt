package domain.discount

import domain.common.Money

class PaymentDiscount : TotalDiscountStrategy {
    override fun apply(
        money: Money,
        context: PaymentDiscountContext,
    ): Money {
        return money * context.paymentType.discountRate
    }
}
