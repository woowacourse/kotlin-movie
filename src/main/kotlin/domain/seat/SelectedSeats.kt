package domain.seat

class SelectedSeats(
    private val seats: List<Seat>,
) {
    val totalPrice: Int
        get() = seats.sumOf { it.grade.price }
}
