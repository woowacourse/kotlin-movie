package movie.domain.payment

import movie.domain.amount.Price

interface PaymentMethod {
    fun applyDiscount(price: Price): Price
}
