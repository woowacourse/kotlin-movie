package domain.model

data class SeatPositions(
    val positions: List<SeatPosition>,
) {
    init {
        require(positions.distinct().size == positions.size) { "중복된 좌석이 존재합니다" }
    }

    fun calculate(): Money {
        var total = 0
        for (position in positions) {
            total += SeatGrade.of(position).price
        }
        return Money(total)
    }
}
