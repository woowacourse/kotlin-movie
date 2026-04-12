package model.seat

import model.Money

class Seats(
    seats: List<Seat>,
) {
    private val value = seats.toList()

    val seatCount: Int = seats.size
    val seatNumbers: List<SeatNumber> = seats.map { it.seatNumber }

    fun findSeat(seatNumber: SeatNumber): Seat =
        value.find { it.seatNumber == seatNumber }
            ?: throw IllegalArgumentException("해당 좌석이 없습니다.")

    fun excludeReserved(reservedSeats: Seats): Seats = Seats(value - reservedSeats.value.toSet())

    fun calculateTotalPrice(): Money = value.fold(Money(0)) { total, seat -> total + seat.seatGrade.price }

    fun add(newSeats: Seats) = Seats(value + newSeats.value)
}
