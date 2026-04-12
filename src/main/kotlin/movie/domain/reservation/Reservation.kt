package movie.domain.reservation

import movie.domain.amount.Price
import movie.domain.movie.Movie
import movie.domain.screening.Screening
import movie.domain.seat.SelectedSeats

class Reservation(
    val movie: Movie,
    val screening: Screening,
    val selectedSeats: SelectedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screening.isTimeOverlapping(other)

    fun calculatePrice(): Price = selectedSeats.totalPrice
}
