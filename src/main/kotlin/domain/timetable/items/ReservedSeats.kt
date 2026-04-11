package domain.timetable.items

import domain.seat.Seat

class ReservedSeats(
    private val reservedSeats: MutableList<Seat> = mutableListOf(),
) {
    fun addSeat(seat: Seat) {
        require(!isReserved(seat)) { "이미 예약된 좌석 입니다." }
        reservedSeats.add(seat)
    }

    private fun isReserved(seat: Seat): Boolean = reservedSeats.any { it.isExistSeat(seat) }
}
