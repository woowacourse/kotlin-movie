package domain

@JvmInline
value class Point(val value: Int) {
    init {
        require(value >= 0) { "포인트는 음수가 될 수 없습니다." }
    }

    fun convertToMoney(): Money = Money(value)
}
