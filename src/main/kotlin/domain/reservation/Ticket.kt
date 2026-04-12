package domain.reservation

import domain.screening.Screening
import domain.seat.SeatPositions

data class Ticket(
    val screening: Screening,
    val seatPositions: SeatPositions,
)
