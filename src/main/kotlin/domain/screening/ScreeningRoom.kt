package domain.screening

import domain.common.TimeRange
import domain.seat.Column
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.Seats

data class ScreeningRoom(
    val id: Long? = null,
    val name: ScreeningRoomName,
    val operatingTime: TimeRange,
    val seats: Seats = createDefaultSeats(),
) {
    companion object {
        private fun createDefaultSeats(): Seats {
            val seats = mutableListOf<Seat>()
            for (row in listOf("A", "B", "C", "D", "E")) {
                for (col in 1..4) {
                    seats.add(Seat(SeatPosition(Row(row), Column(col))))
                }
            }
            return Seats(seats)
        }
    }
}
