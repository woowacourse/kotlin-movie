package movie.domain.screening

import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat

data class Screening(
    val title: String,
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
    val reservedSeats: ReservedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = screeningDateTime.isOverlapping(other.screeningDateTime)

    fun reserve(selectedSeats: List<Seat>): Screening {
        validateReserveAvailable(selectedSeats)
        val finalSeats = reservedSeats.add(selectedSeats)
        return this.copy(reservedSeats = finalSeats)
    }

    private fun validateReserveAvailable(selectedSeats: List<Seat>) {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }
    }

    private fun isValidSeats(selectedSeats: List<Seat>): Boolean = selectedSeats.all { screen.seats.hasSeat(it) }
}
