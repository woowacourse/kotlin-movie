package domain.seat

data class SeatPosition(
    val row: Row,
    val column: Column,
) {
    val grade: SeatGrade get() = SeatGrade.of(this)
    val price: Int get() = grade.price

    override fun toString(): String = "$row$column"
}
