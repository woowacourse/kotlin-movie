package domain.point

import domain.money.Money

@JvmInline
value class Point(
    private val amount: Int,
) {
    init {
        require(amount >= 0) { "포인트는 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun toMoney(useAmount: Int): Money {
        validate(useAmount)
        return Money(useAmount)
    }

    fun subtractPoint(useAmount: Int): Point {
        validate(useAmount)
        return Point(amount - useAmount)
    }

    private fun validate(useAmount: Int) {
        require(useAmount >= 0) { "사용할 포인트는 0보다 작을 수 없습니다." }

        require(useAmount <= this.amount) { "잔여 포인트보다 많은 포인트를 사용할 수 없습니다." }
    }
}
