package movie.domain.seat

import movie.domain.amount.Price

class SelectedSeats(
    private val seats: Set<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
        require(seats.distinct().size == seats.size) { "중복된 좌석을 선택할 수 없습니다." }
    }

    val totalPrice: Price
        get() =
            seats
                .map { it.grade.price }
                .reduce { acc, price -> acc + price }

    fun getSeats(): Set<Seat> = seats
}
