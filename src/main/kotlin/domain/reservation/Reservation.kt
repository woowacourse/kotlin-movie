package domain.reservation

import domain.amount.Money
import domain.screening.Screening
import domain.seat.SelectedSeats

class Reservation(
    private val screening: Screening,
    private val selectedSeats: SelectedSeats
){
    fun isTimeOverlapping(other: Reservation): Boolean {
        return screening.isTimeOverlapping(other.screening)
    }

    fun calculatePrice(): Money {
        return Money(selectedSeats.totalPrice)
    }
}
