package domain.model

data class Seats(
    val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }

    fun contains(position: SeatPosition): Boolean =
        seats.any { it.position == position }

    fun canReserve(position: SeatPosition): Boolean {
        val target = seats.find { it.position == position }
            ?: throw IllegalArgumentException("존재하지 않는 좌석입니다")
        return target.state == ReserveState.AVAILABLE
    }

    fun updateState(
        position: SeatPosition,
        state: ReserveState,
    ): Seats =
        Seats(
            seats =
                seats.map { seat ->
                    if (seat.position == position) {
                        seat.changeState(state)
                    } else {
                        seat
                    }
                },
        )
}
