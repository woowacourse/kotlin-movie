package domain

@JvmInline
value class Money(val value: Int) {
    init {
        require(value >= 0) { "금액은 음수가 될 수 없습니다." }
    }

    operator fun times(rate: Double): Money = Money((value * rate).toInt())

    operator fun minus(other: Money): Money = Money(value - other.value)

    operator fun plus(other: Money): Money = Money(value + other.value)
}
