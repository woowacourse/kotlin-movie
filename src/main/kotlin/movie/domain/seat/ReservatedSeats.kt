package movie.domain.seat

class ReservatedSeats(
    private val seats: List<Seat>,
) {
    fun isAvailable(seat: Seat): Boolean = seats.none { it.seatRow == seat.seatRow && it.seatColumn == seat.seatColumn }

    fun add(selectedSeats: SelectedSeats): ReservatedSeats {
        val updatedSeats = seats.toMutableList()
        selectedSeats.forEach { updatedSeats.add(it) }
        return ReservatedSeats(updatedSeats)
    }
}
