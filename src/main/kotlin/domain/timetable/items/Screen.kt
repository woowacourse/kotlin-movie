package domain.timetable.items

import domain.seat.Seat

class Screen(
    private val seats: List<Seat>,
) {
    fun findSeat(number: String): Boolean = seats.any { it.isExist(number) }
}
