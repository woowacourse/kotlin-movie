package domain.money

@JvmInline
value class Money(
    private val amount: Int,
) {
    init {
        require(amount >= 0) { "가격은 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun getAmount() = amount
}
