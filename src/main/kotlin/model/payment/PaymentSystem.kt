package model.payment

import model.Money
import model.Point

class PaymentSystem(
    private val paymentMethod: PaymentMethod,
) {
    fun pay(
        discountedPrice: Money,
        point: Point,
    ): PayResult {
        val usedPoint = Point(point.value.coerceAtMost(discountedPrice.value))
        val finalPrice = discountedPrice - usedPoint.convertToMoney()
        return PayResult(applyPaymentDiscount(finalPrice), usedPoint)
    }

    private fun applyPaymentDiscount(price: Money): Money =
        price - paymentMethod.calculateDiscountAmount(price)
}
