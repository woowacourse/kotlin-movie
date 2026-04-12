package domain.ticket

import domain.common.Money

data class TicketBucket(
    val tickets: List<Ticket> = emptyList(),
) {

    fun addTicket(newTicket: Ticket): TicketBucket {
        for (ticket in tickets) {
            if (!ticket.isOverlapping(newTicket)) break

            require(ticket.isSameScreening(newTicket)) { "동일한 시간대의 영화는 예매할 수 없습니다." }
            require((ticket.hasSameSeat(newTicket)).not()) { "이미 선택하신 좌석입니다." }
        }
        return TicketBucket(tickets + newTicket)
    }
}
