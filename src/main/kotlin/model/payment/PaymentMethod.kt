package model.payment

import model.Money

sealed interface PaymentMethod {
    fun calculateDiscountAmount(price: Money): Money
}
