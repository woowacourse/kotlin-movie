package movie.domain.payment

import movie.domain.Price

class Payment {
    fun paymentPrice(
        method: PaymentMethod,
        totalPrice: Price,
    ): Price = totalPrice.getDiscountPrice(method.discountRate)
}
