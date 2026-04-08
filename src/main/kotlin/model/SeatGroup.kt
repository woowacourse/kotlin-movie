package model

import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatRow

class SeatGroup(
    seats: List<Seat>,
) {
    private val seats = seats.toList()

    init {
        require(seats.distinct().size == seats.size) { "중복된 좌석은 존재할 수 없습니다." }
    }

    fun reserve(
        row: SeatRow,
        column: SeatColumn,
    ): MovieReservationResult {
        val seat = seats.first { it.row == row && it.column == column }
        return seat.reserve()
    }
}
