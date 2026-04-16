package movie.model.reservation

import movie.model.Money
import movie.model.screening.Screening
import movie.model.seat.Seats
import java.time.LocalDateTime

data class Reservation(
    val screening: Screening,
    val seats: Seats,
) {
    fun movieTitle(): String = screening.movie.title

    fun startDateTime(): LocalDateTime = screening.startDateTime

    fun calculateBasePrice(): Money = seats.calculateTotalPrice()
}
