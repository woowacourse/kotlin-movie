package model.seat

import model.Money

class Seats(private val value: List<Seat>) {
    init {
        require(value.isNotEmpty()) { "좌석 목록이 비어있으면 안됩니다." }
    }

    fun findSeat(seatNumber: SeatNumber): Seat =
        value.find { it.seatNumber == seatNumber }
            ?: throw IllegalArgumentException("해당 좌석이 없습니다.")

    fun excludeReserved(reservedSeatNumbers: Set<SeatNumber>): Seats =
        Seats(value.filter { !reservedSeatNumbers.contains(it.seatNumber) })

    fun calculateTotalPrice(): Money =
        value.fold(Money(0)) { total, seat -> total + seat.seatGrade.price }

    fun seatNumbers(): List<SeatNumber> = value.map { it.seatNumber }
}
