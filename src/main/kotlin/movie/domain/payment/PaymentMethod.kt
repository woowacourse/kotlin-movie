package movie.domain.payment

import movie.domain.amount.Price

sealed interface PaymentMethod {
    fun applyDiscount(price: Price): Price

    class CreditCard : PaymentMethod {
        override fun applyDiscount(price: Price): Price = price.percentOf(95)
    }

    class Cash : PaymentMethod {
        override fun applyDiscount(price: Price): Price = price.percentOf(98)
    }
}
