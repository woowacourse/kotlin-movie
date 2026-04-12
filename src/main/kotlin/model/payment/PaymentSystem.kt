package model.payment

import model.Money
import model.Point

class PaymentSystem {
    fun pay(
        paymentMethod: PaymentMethod,
        discountedPrice: Money,
        point: Point,
    ): PayResult {
        val usedPoint = Point(point.value.coerceAtMost(discountedPrice.value))
        val finalPrice = discountedPrice - usedPoint.convertToMoney()
        return PayResult(applyPaymentDiscount(paymentMethod, finalPrice), usedPoint)
    }

    private fun applyPaymentDiscount(
        paymentMethod: PaymentMethod,
        price: Money,
    ): Money = price - paymentMethod.calculateDiscountAmount(price)
}
