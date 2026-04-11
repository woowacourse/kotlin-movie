package domain.timetable.items

import domain.seat.Seat

class Seats(
    private val seats: List<Seat>,
) {
    fun isExistSeatNumber(seatNumber: String): Boolean = seats.any { it.isExist(seatNumber) }

    fun findSeat(seatNumber: String): Seat = seats.find { it.isExist(seatNumber) } ?: throw IllegalArgumentException("해당 좌석을 찾을 수 없습니다.")
}
