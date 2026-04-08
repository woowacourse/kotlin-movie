package domain.amount

@JvmInline
value class Point(
    val value: Int,
) {
    init {
        require(value >= 0) { "포인트는 0원 이상이어야 합니다." }
    }

    operator fun minus(money: Money): Point {
        val result = this.value - money.value
        if (result < 0) {
            return Point(0)
        }
        return Point(result)
    }
}
