package movie.domain.payment

import movie.domain.amount.Money

interface PaymentMethod {
    fun applyDiscount(price: Money): Money
}
