package domain

data class Seat(
    val row: Row,
    val column: Column,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade
        get() = SeatGrade.of(row = row, column = column)
}

enum class ReserveState {
    RESERVED,
    AVAILABLE,
}
