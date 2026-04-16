package movie.model.payment

import movie.model.Money

sealed interface PaymentMethod {
    fun calculateDiscountAmount(price: Money): Money
}
