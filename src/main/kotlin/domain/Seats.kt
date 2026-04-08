package domain

data class Seats(
    val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }

    fun updateState(position: SeatPosition, state: ReserveState): Seats {
        return Seats(seats = seats.map { seat ->
            if (seat.position == position) seat.changeState(state)
            else seat
        })
    }
}
