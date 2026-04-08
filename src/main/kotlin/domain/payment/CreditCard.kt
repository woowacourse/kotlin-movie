package domain.payment

import domain.amount.Money

class CreditCard : PaymentMethod {
    override fun applyDiscount(price: Money): Money {
        return price.percentOf(95)
    }
}
