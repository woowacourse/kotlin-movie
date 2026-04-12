package model.payment

interface PaymentDiscountable {
    fun applyDiscount(originalMoney: Money): Money
}

class SequentialPaymentDiscount(
    val discountableGroup: List<PaymentDiscountable>,
) {
    fun getDiscountedPrice(originalMoney: Money): Money =
        discountableGroup.fold(originalMoney) { nextPrice, paymentDiscountable ->
            paymentDiscountable.applyDiscount(nextPrice)
        }
}

class PayTypeDiscount(
    val payType: PayType,
) : PaymentDiscountable {
    override fun applyDiscount(originalMoney: Money): Money =
        when (payType) {
            PayType.CREDIT_CARD -> originalMoney applyRate 0.95
            PayType.CASH -> originalMoney applyRate 0.98
        }
}

class PointDiscount(
    val point: Point,
) : PaymentDiscountable {
    override fun applyDiscount(originalMoney: Money): Money =
        originalMoney.minusWithMinimum(
            money = point.toMoney(),
            minimum = Money(0),
        )
}
