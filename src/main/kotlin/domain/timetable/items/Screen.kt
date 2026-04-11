package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.SeatPosition

class Screen(
    private val name: ScreenName,
    private val seats: List<Seat>,
) {
    fun findSeat(number: SeatPosition): Boolean = seats.any { it.isExistSeatPosition(number) }
}

@JvmInline
value class ScreenName(
    private val name: String,
)
