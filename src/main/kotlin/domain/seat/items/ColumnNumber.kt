package domain.seat.items

@JvmInline
value class ColumnNumber(
    private val columnNumber: Int,
) {
    fun isSame(number: Int) = columnNumber == number
}
