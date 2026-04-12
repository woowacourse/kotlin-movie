package movie.domain.seat

data class Seat(
    val seatRow: SeatRow,
    val seatColumn: SeatColumn,
    val grade: SeatGrade,
)
