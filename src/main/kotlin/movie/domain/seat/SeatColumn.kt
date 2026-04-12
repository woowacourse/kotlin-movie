package movie.domain.seat

data class SeatColumn(
    val value: Int,
) {
    init {
        require(value in 1..4) { "좌석 열은 1~4 사이여야 합니다." }
    }
}
