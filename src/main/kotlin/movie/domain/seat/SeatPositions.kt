package movie.domain.seat

class SeatPositions(
    private val positions: List<SeatPosition>,
) {
    init {
        require(positions.isNotEmpty()) { "좌석 위치 목록은 비어 있을 수 없습니다." }
    }

    fun toSeats(seats: Seats): List<Seat> = positions.map { it.toSeat(seats) }

    fun display(): String = positions.joinToString(", ") { "${it.seatRow.value}${it.seatColumn.value}" }
}


