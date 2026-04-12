package model.seat

@JvmInline
value class SeatRow(
    private val row: String,
) : Comparable<SeatRow> {
    init {
        require(row.length == 1) { "행 문자는 1글자이어야 합니다." }
        require(VALID_ROWS.contains(row)) { "행 문자는 A ~ E 사이이어야 합니다." }
    }

    override fun compareTo(other: SeatRow): Int = row.compareTo(other.row)

    override fun toString(): String = row

    companion object {
        private const val VALID_ROWS = "ABCDE"
    }
}
