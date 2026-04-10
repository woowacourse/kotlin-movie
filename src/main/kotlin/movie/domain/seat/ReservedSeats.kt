package movie.domain.seat

class ReservedSeats(
    private val seats: List<Seat>,
) {
    fun isAvailable(seat: Seat): Boolean = seats.none { it.row == seat.row && it.column == seat.column }

    fun add(selectedSeats: List<Seat>): ReservedSeats = ReservedSeats(seats + selectedSeats)

    fun getSeats(): List<Seat> = seats
}
