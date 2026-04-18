package movie.domain

import movie.error.PaymentErrorMessage

@JvmInline
value class Price(
    val amount: Int,
) {
    init {
        require(amount >= 0) { PaymentErrorMessage.INVALID_PRICE }
    }

    operator fun minus(target: Price): Price = Price(this.amount - target.amount)

    operator fun plus(target: Price): Price = Price(this.amount + target.amount)

    fun getDiscountPrice(rate: Float): Price {
        val discountAmount = Price((this.amount * rate).toInt())
        return this - discountAmount
    }
}
