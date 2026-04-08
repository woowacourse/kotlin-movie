package model

@JvmInline
value class SeatRow(
    val row: String,
) {
    init {
        require(row.length == 1) { "행 문자는 1글자이어야 합니다." }
        require(VALID_ROWS.contains(row)) { "행 문자는 A ~ E 사이이어야 합니다." }
    }

    companion object {
        private const val VALID_ROWS = "ABCDE"
    }
}
