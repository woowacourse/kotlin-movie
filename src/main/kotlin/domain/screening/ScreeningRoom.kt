package domain.screening

import domain.common.TimeRange
import domain.seat.Seats

data class ScreeningRoom(
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats,
)
