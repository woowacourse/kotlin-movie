package domain.payment

import domain.amount.Money

class Cash : PaymentMethod {
    override fun applyDiscount(price: Money): Money = price.percentOf(98)
}
