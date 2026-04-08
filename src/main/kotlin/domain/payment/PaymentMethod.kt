package domain.payment

import domain.Money

sealed interface PaymentMethod {
    fun calculateDiscountAmount(price: Money): Money
}


