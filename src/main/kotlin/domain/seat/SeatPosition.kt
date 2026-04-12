package domain.seat

data class SeatPosition(
    val row: Row,
    val column: Column,
) {
    override fun toString(): String = "$row$column"
}
