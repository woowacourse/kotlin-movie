package model.seat

@JvmInline
value class SeatColumn(
    private val column: Int,
) {
    init {
        require(column in MIN_COLUMN..MAX_COLUMN) { "열 번호는 ${MIN_COLUMN}부터 ${MAX_COLUMN}까지 가능합니다." }
    }

    companion object {
        private const val MIN_COLUMN = 1
        private const val MAX_COLUMN = 4
    }
}
