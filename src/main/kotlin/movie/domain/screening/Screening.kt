package movie.domain.screening

import movie.domain.movie.MovieTitle
import movie.domain.reservation.Reservation
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatPositions
import movie.domain.seat.SeatRow
import movie.domain.seat.SelectedSeats
import java.time.LocalDate

class Screening(
    val id: Long = 0L,
    private val movie: MovieTitle,
    private val slot: ScreeningSlot,
    private val reservatedSeats: ReservatedSeats,
) {
    fun isTimeOverlapping(other: Screening): Boolean = slot.isOverlapping(other.slot)

    fun isReserveAvailable(selectedSeats: SelectedSeats): SelectedSeats {
        require(isValidSeats(selectedSeats)) { "존재하지 않는 좌석입니다." }
        require(selectedSeats.all { reservatedSeats.isAvailable(it) }) { "이미 예약된 좌석입니다." }

        return selectedSeats
    }

    fun reserve(selectedSeats: SelectedSeats): Screening =
        Screening(
            id,
            movie,
            slot,
            reservatedSeats.add(selectedSeats),
        )

    fun createReservation(selectedSeats: SelectedSeats): Reservation =
        Reservation(
            this,
            selectedSeats,
        )

    fun toSelectedSeats(positions: SeatPositions): SelectedSeats = SelectedSeats.from(positions, slot.screen.seats)

    fun screeningDateTime(): ScreeningDateTime = slot.screeningDateTime

    private fun isValidSeats(selectedSeats: SelectedSeats): Boolean = selectedSeats.all { slot.hasSeat(it) }

    fun occursOn(date: LocalDate): Boolean = slot.date == date

    fun titleText(): String = movie.toString()

    fun startTimeText(): String = slot.startTime.toString()

    fun dateText(): String = slot.date.toString()

    fun findSeat(
        row: SeatRow,
        column: SeatColumn,
    ): Seat = slot.screen.seats.findSeat(row, column)

    fun isSeatAvailable(seat: Seat): Boolean = reservatedSeats.isAvailable(seat)

    fun endTimeText(): String = slot.endTime.toString()
}
