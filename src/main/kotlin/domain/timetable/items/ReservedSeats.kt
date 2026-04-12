package domain.timetable.items

import domain.seat.items.SeatPosition

class ReservedSeats(
    private val reservedSeats: MutableList<SeatPosition> = mutableListOf(),
) {
    fun addSeat(seatPosition: SeatPosition) {
        require(!isReservedSeatPosition(seatPosition)) { "이미 예약된 좌석 입니다." }
        reservedSeats.add(seatPosition)
    }

    fun isReservedSeatPosition(seatPosition: SeatPosition): Boolean = reservedSeats.any { it.isExistSeatPosition(seatPosition) }
}
