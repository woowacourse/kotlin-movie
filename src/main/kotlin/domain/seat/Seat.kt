package domain.seat

import domain.money.Money
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade

class Seat(
    private val rowNumber: RowNumber,
    private val columnNumber: ColumnNumber,
    private val seatGrade: SeatGrade,
) {
    fun isExist(number: String): Boolean = columnNumber.isSame(number[0]) && rowNumber.isSame(number[1])

    fun getPrice(): Money = seatGrade.price
}
