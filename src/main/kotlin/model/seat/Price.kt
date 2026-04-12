package model.seat

@JvmInline
value class Price(
    val value: Int,
) {
    init {
        require(value >= 0) { "가격은 0원 이상이어야 합니다" }
    }
}
