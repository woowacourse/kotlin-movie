package domain.reservation

import domain.cinema.ScreeningSchedule
import domain.seat.Seat

class ReservationInfo(
    val screening: ScreeningSchedule,
    val seat: Seat,
) {
    fun price(): Int = screening.calculatePrice(seat.grade.price)
}
