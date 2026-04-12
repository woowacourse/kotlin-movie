package domain.screening

import domain.seat.SeatPositions
import domain.seat.Seats

data class ScreeningRoom(
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats,
) {
    fun reserve(positions: SeatPositions): ScreeningRoom = this.copy(seats = seats.reserve(positions))
}
