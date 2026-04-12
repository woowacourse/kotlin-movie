package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.SeatPosition

class ReservedSeats(
    private val reservedSeats: MutableList<Seat> = mutableListOf(),
) {
    fun addSeat(seat: Seat) {
        require(!isReserved(seat)) { "이미 예약된 좌석 입니다." }
        reservedSeats.add(seat)
    }

    fun isReserved(seat: Seat): Boolean = reservedSeats.any { it.isExistSeat(seat) }

    fun isReservedSeatPosition(seatPosition: SeatPosition): Boolean = reservedSeats.any { it.isExistSeatPosition(seatPosition) }
}
