package application

import domain.purchase.PaymentMethod
import util.ErrorMessage

enum class ApiPaymentMethod(
    val value: String,
    private val paymentMethod: PaymentMethod,
) {
    CREDIT_CARD("CREDIT_CARD", PaymentMethod.CARD),
    CASH("CASH", PaymentMethod.CASH),
    ;

    fun toDomain(): PaymentMethod = paymentMethod

    companion object {
        fun from(value: String): ApiPaymentMethod =
            entries.find { it.value == value }
                ?: throw IllegalArgumentException(ErrorMessage.INVALID_PAYMENT_METHOD)
    }
}
