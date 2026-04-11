package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.SeatPosition

class Screen(
    private val seats: List<Seat>,
) {
    fun findSeat(number: SeatPosition): Boolean = seats.any { it.isExistSeatPosition(number) }
}
