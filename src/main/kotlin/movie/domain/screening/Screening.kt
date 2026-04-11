package movie.domain.screening

import movie.domain.movie.MovieTitle
import movie.domain.reservation.Reservation
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SelectedSeats

class Screening(
    val movie: MovieTitle,
    val slot: ScreeningSlot,
    val reservatedSeats: ReservatedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = slot.isOverlapping(other.slot)

    fun isReserveAvailable(selectedSeats: List<Seat>): List<Seat> {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }

        return selectedSeats
    }

    fun reserve(selectedSeats: List<Seat>): Reservation {
        return Reservation(
            this,
            SelectedSeats(selectedSeats),
        )
    }

    private fun isValidSeats(selectedSeats: List<Seat>): Boolean = selectedSeats.all { slot.hasSeat(it) }
}
