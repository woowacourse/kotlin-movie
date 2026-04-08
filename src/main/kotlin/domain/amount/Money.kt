package domain.amount

@JvmInline
value class Money(
    val value: Int,
) {
    init {
        require(value >= 0) { "금액은 0원 이상이어야 합니다." }
    }

    operator fun plus(other: Money): Money {
        return Money(this.value + other.value)
    }

    operator fun minus(other: Money): Money {
        val result = this.value - other.value
        if (result < 0) {
            return Money(0)
        }
        return Money(result)
    }

    fun percentOf(percent: Int): Money {
        return Money(this.value * percent / 100)
    }
}
