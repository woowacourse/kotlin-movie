package movie.domain.reservation

import movie.domain.amount.Price
import movie.domain.movie.Movie
import movie.domain.screening.Screening
import movie.domain.seat.SelectedSeats

class Reservation(
    private val movie: Movie,
    private val screening: Screening,
    private val selectedSeats: SelectedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screening.isTimeOverlapping(other)

    fun calculatePrice(): Price = selectedSeats.totalPrice

    fun getMovie(): Movie = movie

    fun getScreening(): Screening = screening

    fun getSelectedSeats(): SelectedSeats = selectedSeats
}
