package movie.domain.seat

import movie.domain.amount.Price

class SelectedSeats(
    private val seats: Seats,
) {
    val totalPrice: Price = seats.totalPrice

    init {
        // require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
        require(seats.distinct().size == seats.size) { "중복된 좌석을 선택할 수 없습니다." }
    }

    fun getSeats(): Set<Seat> = seats.seats
}
