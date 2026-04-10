package movie.domain.payment

import movie.domain.amount.Price

class CreditCard : PaymentMethod {
    override fun applyDiscount(price: Price): Price = price.percentOf(95)
}
