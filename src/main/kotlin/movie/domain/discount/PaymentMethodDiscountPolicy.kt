package movie.domain.discount

import movie.domain.amount.Price
import movie.domain.payment.PaymentMethod

class PaymentMethodDiscountPolicy : PaymentDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        paymentMethod: PaymentMethod,
    ): Price =
        when (paymentMethod) {
            is PaymentMethod.CreditCard -> price.percentOf(95)
            is PaymentMethod.Cash -> price.percentOf(98)
        }
}
