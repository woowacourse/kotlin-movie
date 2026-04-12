package movie.domain.seat

class SelectedSeats(
    selectedSeats: List<SeatNumber> = emptyList(),
) {
    private val selectedSeats = selectedSeats.toMutableList()

    fun addSelectedSeats(seats: List<SeatNumber>) {
        selectedSeats.addAll(seats)
    }

    fun isReservationSeat(seatNumber: SeatNumber): Boolean = selectedSeats.contains(seatNumber)
}
