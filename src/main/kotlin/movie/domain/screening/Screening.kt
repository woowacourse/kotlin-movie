package movie.domain.screening

import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.Seats

data class Screening(
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
    val reservedSeats: ReservedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screeningDateTime.isOverlapping(other.screeningDateTime)

    fun reserve(selectedSeats: Seats): Screening {
        validateReserveAvailable(selectedSeats)
        val finalSeats = reservedSeats.add(selectedSeats.seats)
        return this.copy(reservedSeats = finalSeats)
    }

    private fun validateReserveAvailable(selectedSeats: Seats) {
        require(isValidSeats(selectedSeats.seats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.seats.all { reservedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }
    }

    private fun isValidSeats(selectedSeats: Set<Seat>): Boolean = selectedSeats.all { screen.seats.hasSeat(it) }
}
