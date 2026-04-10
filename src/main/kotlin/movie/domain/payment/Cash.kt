package movie.domain.payment

import movie.domain.amount.Price

class Cash : PaymentMethod {
    override fun applyDiscount(price: Price): Price = price.percentOf(98)
}
