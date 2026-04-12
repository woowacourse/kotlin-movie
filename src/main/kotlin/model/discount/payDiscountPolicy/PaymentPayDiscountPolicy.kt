package model.discount.payDiscountPolicy

import model.discount.PaymentMethod
import model.seat.Price

// 현금 및 카드 결제 할인 방식
class PaymentPayDiscountPolicy(
    private val paymentMethod: PaymentMethod,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Price): Price =
        Price(
            when (paymentMethod) {
                PaymentMethod.CARD -> (price.value * 0.95).toInt()
                PaymentMethod.CASH -> (price.value * 0.98).toInt()
            },
        )
}
