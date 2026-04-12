package movie.domain.payment

import movie.domain.amount.Money

class CreditCard : PaymentMethod {
    override fun applyDiscount(price: Money): Money = price.percentOf(95)
}
