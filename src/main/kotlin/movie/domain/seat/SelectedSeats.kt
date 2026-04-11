package movie.domain.seat

import movie.domain.amount.Money

class SelectedSeats(
    private val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
        require(seats.distinct().size == seats.size) { "중복된 좌석을 선택할 수 없습니다." }
    }

    val totalPrice: Money
        get() = Money(seats.sumOf { it.grade.price.value })

    fun all(predicate: (Seat) -> Boolean): Boolean = seats.all(predicate)

    fun display(): String = seats.joinToString(", ") { "${it.seatRow.value}${it.seatColumn.value}" }
}
