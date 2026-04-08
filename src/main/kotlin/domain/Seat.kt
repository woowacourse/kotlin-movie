package domain

data class Seat(
    val position: SeatPosition,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade
        get() = SeatGrade.of(position)
}

enum class ReserveState {
    RESERVED,
    AVAILABLE,
}

data class SeatPosition(
    val row: Row,
    val column: Column,
)
