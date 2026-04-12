package domain.ticket

import domain.common.Money
import domain.screening.Screening
import domain.seat.SeatPositions

data class Ticket(
    val screening: Screening,
    val seatPositions: SeatPositions,
) {
    val totalPrice get() = calculate()

    private fun calculate(): Money = seatPositions.calculate()
}
