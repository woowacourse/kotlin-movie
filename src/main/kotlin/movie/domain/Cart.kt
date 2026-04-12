package movie.domain

import movie.domain.seat.SeatNumber

class Cart(
    private val reservations: Reservations = Reservations(),
) {
    fun addReservation(
        schedule: Schedule,
        seats: List<SeatNumber>,
    ) {
        reservations.addReservation(schedule = schedule, seats = seats)
    }

    fun getReservations(): List<Reservation> = reservations.getReservations()

    fun isDuplicateTime(schedule: Schedule): Boolean = reservations.isDuplicateTime(schedule)
}
