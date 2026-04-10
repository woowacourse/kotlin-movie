package domain.payment

import constants.ErrorMessages

enum class PaymentMethod(
    val discountRate: Double,
) {
    CREDIT_CARD(0.05),
    CASH(0.02),
    ;

    companion object {
        fun validate(number: Int): PaymentMethod =
            when (number) {
                1 -> CREDIT_CARD
                2 -> CASH
                else -> throw IllegalArgumentException(ErrorMessages.INVALID_PAYMENT_METHOD.message)
            }
    }

    fun calculateDiscount(price: Int): Int = (price * (1 - discountRate)).toInt()
}
