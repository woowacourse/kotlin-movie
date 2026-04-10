package domain.seat

class SeatCoordinate(val row: Char, val column: Int) {
    init {
        require(row.isUpperCase()) { "열은 한 글자 대문자 알파벳이여야 합니다." }
        require(column > 0) { "행은 양수이여야 합니다." }
    }

    override fun toString(): String {
        return "$row$column"
    }

    companion object {
        private val FORMAT = Regex("^[A-Z][0-9]+$")

        fun from(input: String): SeatCoordinate {
            require(FORMAT.matches(input)) { "입력된 값이 유효하지 않습니다." }
            return SeatCoordinate(input[0], input.substring(1).toInt())
        }
    }
}
