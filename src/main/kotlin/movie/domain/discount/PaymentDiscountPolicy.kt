package movie.domain.discount

import movie.domain.amount.Price
import movie.domain.payment.PaymentMethod

interface PaymentDiscountPolicy {
    fun applyDiscount(
        price: Price,
        paymentMethod: PaymentMethod,
    ): Price
}
