package domain

data class ScreeningRoom(
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats,
) {
    fun reserve(positions: SeatPositions): ScreeningRoom = this.copy(seats = seats.reserve(positions))
}
