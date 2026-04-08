package domain

@JvmInline
value class Column(
    val value: Int,
) {
    init {
        require(value in 1..12) {
            "열은 1~12 사이의 숫자여야 합니다."
        }
    }
}
