package movie.domain.seat

class ReservedSeats(
    private val seats: Set<Seat>,
) {
    fun isAvailable(seat: Seat): Boolean = seats.none { it.row == seat.row && it.column == seat.column }

    fun add(selectedSeats: Set<Seat>): ReservedSeats = ReservedSeats(seats + selectedSeats)

    fun getSeats(): Set<Seat> = seats
}
