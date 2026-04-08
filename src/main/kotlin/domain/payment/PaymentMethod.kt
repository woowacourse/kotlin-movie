package domain.payment

import domain.amount.Money

interface PaymentMethod {
    fun applyDiscount(price: Money): Money
}
