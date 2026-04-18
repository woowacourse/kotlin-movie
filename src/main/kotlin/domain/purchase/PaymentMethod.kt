package domain.purchase

enum class PaymentMethod {
    CARD,
    CASH,
    ;

    fun applyDiscount(price: Int): Int =
        when (this) {
            CARD -> ((1 - CARD_DISCOUNT_PERCENT) * price).toInt()
            CASH -> ((1 - CASH_DISCOUNT_PERCENT) * price).toInt()
        }

    companion object {
        private const val CARD_DISCOUNT_PERCENT = 0.05
        private const val CASH_DISCOUNT_PERCENT = 0.02
    }
}
