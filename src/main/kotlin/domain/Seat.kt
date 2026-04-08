package domain

data class Seat(
    val row: String,
    val column: Int,
) {
    init {
        require(row.matches(Regex("[A-Ja-j]"))) {
            "행은 A~J 사이의 알파벳이어야 합니다."
        }
        require(column in 1..12) {
            "열은 1~12 사이의 숫자여야 합니다."
        }
    }
}
