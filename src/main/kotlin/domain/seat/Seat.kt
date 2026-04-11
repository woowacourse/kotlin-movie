package domain.seat

import domain.money.Money
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition

class Seat(
    private val seatPosition: SeatPosition,
    private val seatGrade: SeatGrade,
) {
    fun isExistSeatPosition(otherSeatPosition: SeatPosition): Boolean = seatPosition.isExistSeatPosition(otherSeatPosition)

    fun isExistSeat(seat: Seat): Boolean = seatPosition.isExistSeatPosition(seat.seatPosition)

    fun addSeatPrice(money: Money): Money = seatGrade.addPrice(money)
}
