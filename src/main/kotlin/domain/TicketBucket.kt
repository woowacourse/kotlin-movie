package domain

data class TicketBucket(
    val tickets: List<Ticket>,
) {
    fun addTicket(newTicket: Ticket): TicketBucket {
        for (ticket in tickets) {
            if (!ticket.screening.screenTimeRange.isOverlapping(newTicket.screening.screenTimeRange)) break

            require(ticket.screening.id == newTicket.screening.id) { "동일한 시간대의 영화는 예매할 수 없습니다." }
        }
        return TicketBucket(tickets + newTicket)
    }
}
