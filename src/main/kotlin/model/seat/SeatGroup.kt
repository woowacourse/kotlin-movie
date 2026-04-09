package model.seat

class SeatGroup(
    seats: List<Seat>,
) : Iterable<Seat> {
    private val seats = seats.toList()

    init {
        require(seats.distinct().size == seats.size) { "중복된 좌석은 존재할 수 없습니다." }
    }

    fun getSeat(
        row: SeatRow,
        column: SeatColumn,
    ): Seat {
        val seat = seats.first { it.isSameSeat(row, column) }
        return seat
    }

    override fun iterator(): Iterator<Seat> = seats.iterator()
}
