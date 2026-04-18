package model.seat

import java.util.Objects

class Seat(
    val row: SeatRow,
    val column: SeatColumn,
    val grade: SeatGrade,
) : Comparable<Seat> {
    override fun equals(other: Any?): Boolean {
        if (other is Seat) {
            return row == other.row && column == other.column
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(row, column)

    override fun compareTo(other: Seat): Int {
        val rowCompare = row.compareTo(other.row)
        if (rowCompare != 0) return rowCompare
        return column.compareTo(other.column)
    }
}
