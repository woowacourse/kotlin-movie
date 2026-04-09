package model.seat

data class SeatNumber(
    val row: Char,
    val column: Int
) {
    init {
        require(row in 'A'..'E') { "행은 알파벳 A~E 사이여야 합니다." }
        require(column in 1..4) { "열은 숫자 1~4 사이여야 합니다." }
    }

    override fun toString(): String = "$row$column"
}
