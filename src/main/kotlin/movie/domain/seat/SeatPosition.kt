package movie.domain.seat

data class SeatPosition(
    val seatRow: SeatRow,
    val seatColumn: SeatColumn,
) {
    fun toSeat(seats: Seats): Seat = seats.findSeat(seatRow, seatColumn)
}

