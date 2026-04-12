package movie.domain.seat

data class SeatRow(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "좌석 행은 비어 있을 수 없습니다." }
        require(value in "A".."E") { "좌석 행은 A~E 사이여야 합니다." }
    }
}
