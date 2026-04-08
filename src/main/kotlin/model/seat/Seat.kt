package model.seat

import java.util.Objects

class Seat(
    val row: SeatRow,
    val column: SeatColumn,
    val grade: SeatGrade,
    private var state: SeatState,
) {
    fun reserve(): Boolean {
        if (state == SeatState.AVAILABLE) {
            state = SeatState.RESERVED
            return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (other is Seat) {
            return row == other.row && column == other.column
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(row, column)
}
