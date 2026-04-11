package movie.domain.screening

import movie.domain.movie.MovieTitle
import movie.domain.reservation.Reservation
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.SelectedSeats

class Screening(
    val movie: MovieTitle,
    val slot: ScreeningSlot,
    val reservatedSeats: ReservatedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = slot.isOverlapping(other.slot)

    fun isReserveAvailable(selectedSeats: SelectedSeats): SelectedSeats {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }

        return selectedSeats
    }

    fun reserve(selectedSeats: SelectedSeats): Reservation {
        return Reservation(
            this,
            selectedSeats,
        )
    }

    private fun isValidSeats(selectedSeats: SelectedSeats): Boolean = selectedSeats.all { slot.hasSeat(it) }
}
