package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.SeatPosition

class Screen(
    private val name: ScreenName,
    private val seats: Seats,
) {
    fun isExistSeat(number: SeatPosition): Boolean = seats.isExistSeatNumber(number)

    fun findSeat(position: SeatPosition): Seat = seats.findSeat(position)
}

@JvmInline
value class ScreenName(
    private val name: String,
)
