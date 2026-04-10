package model.seat

import model.Money

class Seats(
    val value: List<Seat>,
) {
    val seatCount: Int = value.size
    val seatNumbers: List<SeatNumber> = value.map { it.seatNumber }

    fun findSeat(seatNumber: SeatNumber): Seat =
        value.find { it.seatNumber == seatNumber }
            ?: throw IllegalArgumentException("해당 좌석이 없습니다.")

    fun excludeReserved(reservedSeatNumbers: Set<SeatNumber>): Seats = Seats(value.filter { !reservedSeatNumbers.contains(it.seatNumber) })

    fun calculateTotalPrice(): Money = value.fold(Money(0)) { total, seat -> total + seat.seatGrade.price }
}
