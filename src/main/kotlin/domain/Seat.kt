package domain

data class Seat(
    val row: Row,
    val column: Column,
) {
    val grade: SeatGrade
        get() = SeatGrade.of(row = row, column = column)
}
