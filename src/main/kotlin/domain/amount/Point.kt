package domain.amount

@JvmInline
value class Point(
    val value: Int,
) {
    init {
        require(value >= 0) { "포인트는 0원 이상이어야 합니다." }
    }

    fun usableAmount(price: Money): Point {
        if (value >= price.value) {
            return Point(price.value)
        }
        return Point(value)
    }
}
