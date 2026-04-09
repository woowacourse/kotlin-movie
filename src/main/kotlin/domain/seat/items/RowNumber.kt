package domain.seat.items

@JvmInline
value class RowNumber(
    private val rowNumber: String,
) {
    fun isSame(number: String): Boolean = rowNumber == number
}
