package movie.domain.discount

import movie.domain.payment.PaymentMethod

object PaymentMethodInputParser {
    fun parse(input: Int): PaymentMethod =
        when (input) {
            1 -> PaymentMethod.CreditCard
            2 -> PaymentMethod.Cash
            else -> throw IllegalArgumentException("유효하지 않은 결제 수단입니다.")
        }
}
