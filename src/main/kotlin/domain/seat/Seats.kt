package domain.seat

data class Seats(
    val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }

    fun canReserve(position: SeatPosition): Boolean {
        val target =
            seats.find { it.position == position }
                ?: throw IllegalArgumentException("존재하지 않는 좌석입니다")
        return target.canReserve()
    }

    fun reserve(positions: SeatPositions): Seats =
        Seats(
            seats =
                seats.map { seat ->
                    if (positions.contains(seat.position)) {
                        seat.changeState(ReserveState.RESERVED)
                    } else {
                        seat
                    }
                },
        )
}
