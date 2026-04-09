package movie.domain.seat

class ReservatedSeats(
    private val seats: List<Seat>,
) {
    fun isAvailable(seat: Seat): Boolean = seats.none { it.row == seat.row && it.column == seat.column }

    fun add(selectedSeats: List<Seat>): List<Seat> = ReservatedSeats(seats + selectedSeats).seats
}
