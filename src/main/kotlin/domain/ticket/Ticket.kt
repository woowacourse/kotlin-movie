package domain.ticket

import domain.common.Money
import domain.screening.Screening
import domain.seat.SeatPositions

data class Ticket(
    val screening: Screening,
    val seatPositions: SeatPositions,
) {
    val totalPrice = calculate()

    fun isOverlapping(other: Ticket): Boolean =
        screening.isOverlapping(other.screening)
    fun isSameScreening(other: Ticket): Boolean =
        screening.isSame(other.screening)
    fun hasSameSeat(other: Ticket): Boolean =
        seatPositions.hasAnyOverlap(other.seatPositions)
    private fun calculate(): Money {
        var total = Money(0)
        seatPositions.positions.forEach { position ->
            total += position.price
        }

        return total
    }

}
