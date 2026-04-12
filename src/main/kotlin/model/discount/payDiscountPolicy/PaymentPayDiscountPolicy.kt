package model.discount.payDiscountPolicy

import model.discount.PaymentMethod

// 현금 및 카드 결제 할인 방식
class PaymentPayDiscountPolicy(
    private val paymentMethod: PaymentMethod,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Int): Int =
        when (paymentMethod) {
            PaymentMethod.CARD -> (price * 0.95).toInt()
            PaymentMethod.CASH -> (price * 0.98).toInt()
        }
}
