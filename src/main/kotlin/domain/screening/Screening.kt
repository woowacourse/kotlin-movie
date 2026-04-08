package domain.screening

import domain.reservation.Reservation
import domain.seat.ReservatedSeats
import domain.seat.Seat

class Screening(
    val id: Int,
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
    val reservatedSeats: ReservatedSeats
) {
    fun isReserveAvailable(selectedSeats: List<Seat>): List<Seat> {

        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }

        return selectedSeats
    }

    fun reserve(selectedSeats: List<Seat>): Reservation {
        reservatedSeats.add(selectedSeats)

        return Reservation(this, selectedSeats)

    }
}
