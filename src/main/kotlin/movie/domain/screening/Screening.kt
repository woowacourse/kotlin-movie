package movie.domain.screening

import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat

data class Screening(
    val title: String,
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
    val reservatedSeats: ReservatedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screeningDateTime.isOverlapping(other.screeningDateTime)

    fun reserve(selectedSeats: List<Seat>): Screening {
        validateReserveAvailable(selectedSeats)
        val finalSeats = reservatedSeats.add(selectedSeats)
        return this.copy(reservatedSeats = finalSeats)
    }

    private fun validateReserveAvailable(selectedSeats: List<Seat>) {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }
    }

    private fun isValidSeats(selectedSeats: List<Seat>): Boolean = selectedSeats.all { screen.seats.hasSeat(it) }
}
