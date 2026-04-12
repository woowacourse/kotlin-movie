package movie.domain.seat

import movie.domain.amount.Price

class SelectedSeats(
    private val seats: Seats,
) {
    val totalPrice: Price = seats.totalPrice

    init {
        require(seats.distinct().size == seats.size) { "중복된 좌석을 선택할 수 없습니다." }
    }

    fun getSeats(): Set<Seat> = seats.seats
}
