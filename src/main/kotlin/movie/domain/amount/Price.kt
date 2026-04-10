package movie.domain.amount

@JvmInline
value class Price(
    val value: Int,
) {
    init {
        require(value >= 0) { "금액은 0원 이상이어야 합니다." }
    }

    operator fun plus(other: Price): Price = Price(this.value + other.value)

    operator fun minus(other: Price): Price {
        val result = this.value - other.value
        if (result < 0) {
            return Price(0)
        }
        return Price(result)
    }

    fun percentOf(percent: Int): Price = Price(this.value * percent / 100)
}
