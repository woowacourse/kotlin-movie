package domain.seat

data class Seat(
    val position: SeatPosition,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade
        get() = SeatGrade.of(position)

    fun changeState(state: ReserveState): Seat = this.copy(state = state)
    fun canReserve(): Boolean = state == ReserveState.AVAILABLE
}

enum class ReserveState {
    RESERVED,
    AVAILABLE,
}

data class SeatPosition(
    val row: Row,
    val column: Column,
) {
    override fun toString(): String = "$row$column"
}
