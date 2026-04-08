package model

import java.util.Objects

class Seat(
    val row: SeatRow,
    val column: SeatColumn,
    val grade: SeatGrade,
    val state: SeatState,
) {
    override fun equals(other: Any?): Boolean {
        if (other is Seat) {
            return row == other.row && column == other.column
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(row, column)
}
