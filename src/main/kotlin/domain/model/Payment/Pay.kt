package domain.model.payment

data class Pay(
    private val point: Int,
    private val paymentMethod: PaymentMethod,
) {
    init {
        require(point >= 0) { "포인트는 0 이상이어야 합니다." }
    }

    // 결제 순서
    // 1) 포인트 차감
    // 2) 결제수단 할인 적용
    fun payAmountApply(payAmount: Int): Int {
        val pointApplyPay = pointAmount(payAmount).coerceAtLeast(0)
        val amountPay = paymentMethodDiscountAmount(pointApplyPay)
        return pointApplyPay.minus(amountPay).coerceAtLeast(0)
    }

    private fun pointAmount(payAmount: Int): Int = payAmount - point

    // 결제수단 할인 적용 신용카드 5% 현금 2%
    private fun paymentMethodDiscountAmount(payAmount: Int): Int = (payAmount * paymentMethod.methodDiscountAmount()).toInt()
}

enum class PaymentMethod {
    CARD,
    CASH,
    ;

    fun methodDiscountAmount(): Double =
        when (this) {
            CARD -> 0.05
            CASH -> 0.02
        }
}
