package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point

data class PaymentResult(
    val totalPrice: Money,
    val usedPoint: Point,
    val paymentMethod: PaymentMethod,
) {
    fun paymentMethodName(): String =
        when (paymentMethod) {
            is CreditCard -> "CREDIT_CARD"
            is Cash -> "CASH"
            else -> "UNKNOWN"
        }
}
