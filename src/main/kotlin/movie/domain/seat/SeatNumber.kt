package movie.domain.seat

data class SeatNumber(
    val row: Char,
    val col: Int,
) {
    init {
        require(row in 'A'..'Z') { "좌석 번호의 행은 A부터 Z사이의 알파뱃이어야 합니다." }
        require(col > 0) { "좌석 번호의 열은 0보다 큰 숫자여야 합니다." }
    }

    override fun toString(): String = "$row$col"
}
