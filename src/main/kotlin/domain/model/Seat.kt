package domain.model

data class Seat(
    val position: SeatPosition,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade
        get() = SeatGrade.of(position)

    fun changeState(state: ReserveState): Seat = this.copy(state = state)
}

enum class ReserveState {
    RESERVED,
    AVAILABLE,
}

data class SeatPosition(
    val row: Row,
    val column: Column,
) {
    override fun toString(): String {
        return "$row$column"
    }
}
