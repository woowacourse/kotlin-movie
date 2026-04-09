package movie.domain.movie

import movie.domain.Price
import movie.domain.seat.number.SeatNumber

class Reservation(
    val screeningMovie: ScreeningMovie,
    val seatNumbers: List<SeatNumber>,
) {
    fun getTotalPrice(): Price = screeningMovie.calculatePrice(targetSeatNumbers = seatNumbers)
}
