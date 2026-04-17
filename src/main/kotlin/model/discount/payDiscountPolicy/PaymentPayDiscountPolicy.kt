package model.discount.payDiscountPolicy

import model.discount.PaymentMethod
import model.seat.Price

class PaymentPayDiscountPolicy(
    private val paymentMethod: PaymentMethod,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Price): Price =
        Price(
            when (paymentMethod) {
                PaymentMethod.CREDIT_CARD -> (price.value * 0.95).toInt()
                PaymentMethod.CASH -> (price.value * 0.98).toInt()
            },
        )
}
