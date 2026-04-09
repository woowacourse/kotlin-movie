package movie.domain.movie

import movie.domain.seat.number.SeatNumber

class Ticket(
    private val reservations: Reservations = Reservations(),
) {
    fun getReservations(): List<Reservation> = reservations.getReservations()

    fun isDupTime(movieTime: MovieTime): Boolean = reservations.isDupTime(movieTime = movieTime)

    fun resetSeat() {
        reservations.reset()
    }

    fun addReservation(
        screeningMovie: ScreeningMovie,
        seatNumbers: List<SeatNumber>,
    ): Reservation {
        val reservation =
            Reservation(
                screeningMovie = screeningMovie,
                seatNumbers = seatNumbers,
            )

        reservations.addReservation(reservation)
        screeningMovie.addReservedSeats(seatNumbers)

        return reservation
    }
}
