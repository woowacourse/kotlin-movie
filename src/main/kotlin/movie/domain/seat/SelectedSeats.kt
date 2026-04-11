package movie.domain.seat

class SelectedSeats(selectedSeats: List<SeatNumber> = emptyList()) {
    private val _selectedSeats = selectedSeats.toMutableList()

    fun addSelectedSeats(seats: List<SeatNumber>) {
        _selectedSeats.addAll(seats)
    }
}
