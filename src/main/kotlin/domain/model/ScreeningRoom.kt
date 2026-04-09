package domain.model

data class ScreeningRoom(
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats,
)
