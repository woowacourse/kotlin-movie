package domain.seat.items

@JvmInline
value class RowNumber(
    private val rowNumber: Char,
) {
    fun isSame(number: Char): Boolean = rowNumber == number
}
