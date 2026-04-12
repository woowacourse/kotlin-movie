package model.payment

interface PaymentDiscountable {
    fun applyDiscount(originalMoney: Money): Money
}

class PayTypeDiscount(
    private val payType: PayType,
) : PaymentDiscountable {
    override fun applyDiscount(originalMoney: Money): Money =
        when (payType) {
            PayType.CREDIT_CARD -> originalMoney applyRate (1 - CREDIT_CARD_DISCOUNT_RATIO)
            PayType.CASH -> originalMoney applyRate (1 - CASH_DISCOUNT_RATIO)
        }

    companion object {
        const val CREDIT_CARD_DISCOUNT_RATIO = 0.05
        const val CASH_DISCOUNT_RATIO = 0.02
    }
}

class PointDiscount(
    private val point: Point,
) : PaymentDiscountable {
    override fun applyDiscount(originalMoney: Money): Money =
        originalMoney.minusWithMinimum(
            money = point.toMoney(),
            minimum = Money(0),
        )
}
