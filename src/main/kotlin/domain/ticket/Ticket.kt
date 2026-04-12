package domain.ticket

import domain.common.Money
import domain.screening.Screening
import domain.seat.SeatPositions

data class Ticket(
    val screening: Screening,
    val seatPositions: SeatPositions,
) {
    val totalPrice get() = calculate()

    fun isOverlapping(other: Ticket): Boolean =
        screening.screenTimeRange.isOverlapping(other.screening.screenTimeRange)

    fun isSameScreening(other: Ticket): Boolean =
        screening.id == other.screening.id

    fun hasSameSeat(other: Ticket): Boolean =
        seatPositions.positions.any { it in other.seatPositions.positions }
    private fun calculate(): Money = seatPositions.calculate()
}
