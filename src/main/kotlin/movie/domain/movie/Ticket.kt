package movie.domain.movie

import movie.domain.Price

class Ticket(
    private val reservations: List<Reservation> = emptyList(),
) {
    init {
        require(reservations.isNotEmpty()) { "예매 내역이 없는 티켓은 생성할 없습니다." }
    }
    fun getTotalPrice(): Price {
        return reservations
            .map { it.getTotalPrice() }
            .fold(Price(0)) { total, price ->
                total.sumPrice(price)
            }
    }
}
