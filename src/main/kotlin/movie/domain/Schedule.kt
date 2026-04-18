package movie.domain

import movie.error.ScheduleErrorMessage
import movie.error.SeatErrorMessage
import movie.domain.seat.SeatNumber
import movie.domain.seat.SelectedSeats
import java.time.LocalDateTime

class Schedule(
    val id: Long,
    val movie: Movie,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val selectedSeat: SelectedSeats = SelectedSeats(),
    val theater: Theater = Theater(),
) {
    init {
        require(startTime < endTime) { ScheduleErrorMessage.INVALID_TIME_RANGE }
    }

    fun isReservationSeats(seatNumbers: List<SeatNumber>): Boolean = seatNumbers.any { selectedSeat.isReservationSeat(it) }

    fun isReservationSeat(seatNumber: SeatNumber): Boolean = selectedSeat.isReservationSeat(seatNumber)

    fun isDuplicateTime(target: Schedule): Boolean =
        target.endTime in startTime..endTime ||
            target.startTime in startTime..endTime

    fun addSeats(seats: List<SeatNumber>) {
        require(seats.all { theater.isValidSeatNumber(it) }) { SeatErrorMessage.INVALID_NUMBER }

        selectedSeat.addSelectedSeats(seats)
    }

    fun getPrice(seatNumber: SeatNumber): Price = theater.getPrice(seatNumber)
}
