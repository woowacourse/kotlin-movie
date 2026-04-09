package movie.domain.movie

import movie.domain.Price

class Reservations(
    reservations: List<Reservation> = emptyList()
) {
    private val _reservations = reservations.toMutableList()

    fun addReservation(reservation: Reservation) {
        _reservations.add(reservation)
    }

    fun getTotalPrice(): Price {
        return _reservations
            .map { it.getTotalPrice() }
            .fold(Price(0)) { total, price ->
                total.sumPrice(price)
            }
    }
}