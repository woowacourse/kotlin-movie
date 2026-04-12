package domain.reservation

import domain.screening.Screening
import domain.seat.SeatPositions

data class TicketBucket(
    val tickets: List<Ticket> = emptyList(),
) {
    fun addTicket(
        screening: Screening,
        positions: SeatPositions,
    ): TicketBucket {
        for (ticket in tickets) {
            if (!ticket.screening.screenTimeRange.isOverlapping(screening.screenTimeRange)) continue

            require(ticket.screening.id == screening.id) { "동일한 시간대의 영화는 예매할 수 없습니다." }
            require(positions.positions.none { it in ticket.seatPositions.positions })
        }

        return TicketBucket(tickets + Ticket(screening, positions))
    }
}
