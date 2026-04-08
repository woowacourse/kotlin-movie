package domain.reservation

import domain.amount.Money
import domain.screening.Screening
import domain.seat.SelectedSeats

class Reservation(
    private val screening: Screening,
    private val selectedSeats: SelectedSeats
){
    fun calculatePrice(): Money {
        return Money(selectedSeats.totalPrice)
    }
}
