package movie.model.screening

import movie.model.movie.Movie
import movie.model.reservation.Reservation
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Screening(
    val id: Long? = null,
    val movie: Movie,
    val startDateTime: LocalDateTime,
    val seatMap: ScreeningSeatMap,
) {
    val endDateTime: LocalDateTime
        get() = startDateTime.plusMinutes(movie.runningTime.minute)

    val showDate: LocalDate
        get() = startDateTime.toLocalDate()

    val startShowTime: LocalTime
        get() = startDateTime.toLocalTime()

    fun reserve(seatNumbers: List<SeatNumber>): Reservation {
        val newSeatMap = seatMap.reserve(seatNumbers)
        val seats = seatNumbers.map { seatMap.screen.seats.findSeat(it) }

        return Reservation(this.copy(seatMap = newSeatMap), Seats(seats))
    }

    fun hasOverlappingWith(other: Screening): Boolean {
        if (movie == other.movie) return false
        return startDateTime < other.endDateTime && endDateTime > other.startDateTime
    }

    fun isSameScreening(other: Screening): Boolean =
        this.movie == other.movie &&
            this.startDateTime == other.startDateTime
}
