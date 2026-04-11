package model.seat

import model.Money

class Seats(
    private val value: List<Seat>,
) {
    val seatCount: Int = value.size
    val seatNumbers: List<SeatNumber> = value.map { it.seatNumber }

    fun findSeat(seatNumber: SeatNumber): Seat =
        value.find { it.seatNumber == seatNumber }
            ?: throw IllegalArgumentException("해당 좌석이 없습니다.")

    fun excludeReserved(reservedSeats: Seats): Seats = Seats(value - reservedSeats.value.toSet())

    fun calculateTotalPrice(): Money = value.fold(Money(0)) { total, seat -> total + seat.seatGrade.price }

    fun addSeats(seatsToAdd: Seats) = Seats(value + seatsToAdd.value)
}
