package model.seat

data class Seat(
    val position: SeatPosition,
    val grade: SeatGrade,
) {
    fun isEqual(position: SeatPosition): Boolean = this.position == position
}
