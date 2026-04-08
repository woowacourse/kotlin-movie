package domain

data class SeatPositions(
    val positions: List<SeatPosition>,
) {
    init {
        require(positions.distinct().size == positions.size) { "중복된 좌석이 존재합니다" }
    }
}
