package movie.domain.seat.number

data class SeatNumber(
    private val row: Row,
    private val col: Column,
) {
    override fun toString(): String = row.toString() + col.toString()
}
