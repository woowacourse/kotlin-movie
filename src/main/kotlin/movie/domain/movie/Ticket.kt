package movie.domain.movie

import movie.domain.Price

class Ticket(
    private val reservations: Reservations = Reservations(),
) {
    fun getTotalPrice(): Price {
        return reservations.getTotalPrice()
    }
}
