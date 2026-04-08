package domain.reservation

import domain.Money
import domain.screening.Screening
import domain.seat.Seats
import java.time.LocalDateTime

data class Reservation(
    val screening: Screening,
    val seats: Seats
) {
    fun movieTitle(): String = screening.movie.title

    fun startDateTime(): LocalDateTime = screening.startDateTime

    fun calculateBasePrice(): Money = seats.calculateTotalPrice()
}
