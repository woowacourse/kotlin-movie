package movie.domain

@JvmInline
value class Price(val amount: Int) {
    init {
        require(amount >= 0) { "가격은 0원 이상이어야 합니다." }
    }

    operator fun minus(target: Price): Price {
        return Price(this.amount - target.amount)
    }

    operator fun plus(target: Price): Price {
        return Price(this.amount + target.amount)
    }

    fun getDiscountPrice(rate: Float): Price {
        val discountAmount = Price((this.amount * rate).toInt())
        return this - discountAmount
    }
}
