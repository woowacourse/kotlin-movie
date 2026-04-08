package domain.seat

class ReservatedSeats(
    private val seats: List<Seat>,
) {
    fun isAvailable(seat: Seat): Boolean {
        return seats.none { it.row == seat.row && it.column == seat.column }
    }

    fun add(seat: Seat): ReservatedSeats {
        return ReservatedSeats(seats + seat)
    }
}
