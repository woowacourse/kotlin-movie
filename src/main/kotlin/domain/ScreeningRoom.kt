package domain

data class ScreeningRoom(
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats,
)
