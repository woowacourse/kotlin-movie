package domain.seat

data class Seat(
    val position: SeatPosition,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade
        get() = SeatGrade.Companion.of(position)

    fun changeState(state: ReserveState): Seat = this.copy(state = state)

    fun canReserve(): Boolean = state == ReserveState.AVAILABLE
}
