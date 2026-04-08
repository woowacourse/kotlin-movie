package model.seat

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SeatGroup(
    seats: List<Seat>,
) {
    private val seats = seats.toList()

    init {
        require(seats.distinct().size == seats.size) { "중복된 좌석은 존재할 수 없습니다." }
    }

    fun getSeat(
        row: SeatRow,
        column: SeatColumn,
    ): Seat {
        val seat = seats.first { it.row == row && it.column == column }
        return seat
    }
}
