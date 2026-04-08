package domain.seat.items

@JvmInline
value class ColumnNumber(
    private val columnNumber: Char,
) {
    fun isSame(number: Char) = columnNumber == number
}
