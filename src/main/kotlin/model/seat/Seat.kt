package model.seat

import model.MovieReservationResult
import java.util.Objects

class Seat(
    val row: SeatRow,
    val column: SeatColumn,
    val grade: SeatGrade,
    var state: SeatState,
) {
    fun reserve(): MovieReservationResult {
        if (state == SeatState.AVAILABLE) {
            state = SeatState.RESERVED
            return MovieReservationResult.SUCCESS
        }

        return MovieReservationResult.FAILED
    }

    override fun equals(other: Any?): Boolean {
        if (other is Seat) {
            return row == other.row && column == other.column
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(row, column)
}
