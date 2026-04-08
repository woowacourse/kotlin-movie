package model.seat

import java.util.Objects

class Seat(
    private val row: SeatRow,
    private val column: SeatColumn,
    private var state: SeatState,
    grade: SeatGrade,
) {
    val price: Int =
        when (grade) {
            SeatGrade.S -> 18_000
            SeatGrade.A -> 15_000
            SeatGrade.B -> 12_000
        }

    fun reserve(): Boolean {
        if (state == SeatState.AVAILABLE) {
            state = SeatState.RESERVED
            return true
        }
        return false
    }

    fun isSameSeat(
        row: SeatRow,
        column: SeatColumn,
    ): Boolean = this.row == row && this.column == column

    override fun equals(other: Any?): Boolean {
        if (other is Seat) {
            return row == other.row && column == other.column
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(row, column)
}
