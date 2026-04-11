package movie.domain

class Cart(
    val reservations: Reservations = Reservations()
) {
    fun addReservation(reservation: Reservation) {
        reservations.addReservation(reservation)
    }
}
