package domain.seat

class Seat(val row: Char, val column: Int, val grade: SeatGrade, val isReserved: Boolean) {
    init {
        require(row.isUpperCase()) { "열은 한 글자 대문자 알파벳이여야 합니다." }
        require(column > 0) { "행은 양수이여야 합니다." }
    }
}
