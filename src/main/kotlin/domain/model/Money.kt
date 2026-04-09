package domain.model

data class Money(
    val amount: Int,
) {
    init {
        require(amount >= 0) { "돈은 음수일 수 없습니다." }
    }

    operator fun plus(other: Money): Money = Money(this.amount + other.amount)

    operator fun plus(other: Int): Money = Money(this.amount + other)

    operator fun minus(other: Money): Money = Money(this.amount - other.amount)

    operator fun minus(other: Int): Money = Money(this.amount - other)

    operator fun times(other: Double): Money = Money((this.amount * other).toInt())
}
