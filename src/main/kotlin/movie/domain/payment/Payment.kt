package movie.domain.payment

import movie.domain.Price

class Payment {
    fun paymentPrice(
        method: PaymentMethod,
        totalPrice: Price,
    ): Price {
        return totalPrice.getDiscountPrice(method.discountRate)
    }
}
