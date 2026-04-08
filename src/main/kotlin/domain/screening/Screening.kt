package domain.screening

import domain.reservation.Reservation
import domain.seat.ReservatedSeats
import domain.seat.Seat
import domain.seat.SelectedSeats

class Screening(
    val id: Int,
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
    val reservatedSeats: ReservatedSeats
) {
    fun isReserveAvailable(selectedSeats: List<Seat>): List<Seat> {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }

        return selectedSeats
    }

    fun reserve(selectedSeats: List<Seat>): Reservation {
        val finalSeats = reservatedSeats.add(selectedSeats)
        return Reservation(this, SelectedSeats(finalSeats))
    }

    private fun isValidSeats(selectedSeats: List<Seat>): Boolean {
        return selectedSeats.all { screen.seats.hasSeat(it) }
    }
}
