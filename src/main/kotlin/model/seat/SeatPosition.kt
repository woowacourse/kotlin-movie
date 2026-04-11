package model.seat

data class SeatPosition(
    val row: SeatRow,
    val column: SeatColumn,
) : Comparable<SeatPosition> {
    override fun compareTo(other: SeatPosition): Int {
        if (row == other.row) {
            return column.compareTo(other.column)
        }
        return row.compareTo(other.row)
    }
}

@JvmInline
value class SeatColumn(
    private val column: Int,
) : Comparable<SeatColumn> {
    init {
        require(column in MIN_COLUMN..MAX_COLUMN) { "열 번호는 ${MIN_COLUMN}부터 ${MAX_COLUMN}까지 가능합니다." }
    }

    override fun compareTo(other: SeatColumn): Int = column.compareTo(other.column)

    override fun toString(): String = column.toString()

    companion object {
        private const val MIN_COLUMN = 1
        private const val MAX_COLUMN = 4
    }
}

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
