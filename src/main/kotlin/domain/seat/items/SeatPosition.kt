package domain.seat.items

class SeatPosition(
    private val rowNumber: RowNumber,
    private val columnNumber: ColumnNumber,
) {
    fun isExistSeatPosition(seatNumber: String): Boolean {
        val matchResult = REGEX.matchEntire(seatNumber.trim()) ?: throw IllegalArgumentException("잘못된 좌석 번호 형식입니다.")

        val (row, col) = matchResult.destructured
        return rowNumber.isSame(row) && columnNumber.isSame(col.toInt())
    }

    companion object {
        private const val SEAT_POSITION_REGEX = "^([A-Z]+)(\\d+)$"
        private val REGEX = Regex(SEAT_POSITION_REGEX)
    }
}
