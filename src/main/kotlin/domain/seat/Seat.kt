package domain.seat

import domain.money.Money
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition

class Seat(
    private val seatPosition: SeatPosition,
    private val seatGrade: SeatGrade,
) {
    fun isExist(number: String): Boolean = seatPosition.isExistSeatPosition(number)

    fun addSeatPrice(money: Money): Money = seatGrade.addPrice(money)
}
