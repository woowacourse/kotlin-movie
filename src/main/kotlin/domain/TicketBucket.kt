package domain

data class TicketBucket(
    val tickets: List<Ticket> = emptyList(),
) {
    val totalPrice get() = calculate()

    fun addTicket(newTicket: Ticket): TicketBucket {
        for (ticket in tickets) {
            if (!ticket.screening.screenTimeRange.isOverlapping(newTicket.screening.screenTimeRange)) break

            require(ticket.screening.id == newTicket.screening.id) { "동일한 시간대의 영화는 예매할 수 없습니다." }
            require(newTicket.seatPositions.positions.none { it in ticket.seatPositions.positions }) { "이미 선택하신 좌석입니다." }
        }
        return TicketBucket(tickets + newTicket)
    }

    private fun calculate(): Money {
        var total = Money(0)
        for (ticket in tickets) {
            total += ticket.totalPrice
        }

        return total
    }
}
