package domain.seat

class Seat(
    val row: String,
    val column: Int,
    val grade: SeatGrade,
) {
    init {
        require(row.isNotBlank()) { "좌석 행은 비어 있을 수 없습니다." }
        require(row in "A".."E") { "좌석 행은 A~E 사이여야 합니다." }
        require(column in 1..4) { "좌석 열은 1~4 사이여야 합니다." }
    }
}
