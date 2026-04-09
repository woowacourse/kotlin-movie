package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.screening.Screening
import movie.domain.seat.SelectedSeats

class Reservation(
    private val screening: Screening,
    private val selectedSeats: SelectedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screening.isTimeOverlapping(other)

    fun calculatePrice(): Money = Money(selectedSeats.totalPrice)

    fun display(): String =
        "- [${screening.title}] ${screening.screeningDateTime.date} ${screening.screeningDateTime.startTime}  좌석: ${selectedSeats.display()}"

    fun getScreening(): Screening = screening
}
