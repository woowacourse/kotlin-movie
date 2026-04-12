package movie.domain.payment

import movie.domain.amount.Money

interface PaymentMethod {
    fun applyDiscount(price: Money): Money

    companion object {
        fun from(input: Int): PaymentMethod =
            when (input) {
                1 -> CreditCard()
                2 -> Cash()
                else -> throw IllegalArgumentException("유효하지 않은 결제 수단입니다.")
            }
    }
}
