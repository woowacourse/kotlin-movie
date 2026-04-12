package movie.domain.seat

class ReservedSeats(
    private val seats: Seats,
) {
    fun isAvailable(seat: Seat): Boolean = seats.isAvailable(seat)

    fun add(selectedSeats: Set<Seat>): ReservedSeats = ReservedSeats(Seats(seats.seats + selectedSeats))

    fun getSeats(): Set<Seat> = seats.seats
}
