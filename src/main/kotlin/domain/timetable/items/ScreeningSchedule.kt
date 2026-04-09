package domain.timetable.items

import domain.movie.Movie
import domain.seat.Seat
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: MutableList<Seat> = mutableListOf()

    fun isScreeningMovieTitle(title: String) = movie.isValidTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun isReservedSeat(seatNumber: String) = reservedSeat.any { it.isExist(seatNumber) }

    fun reserveSeat(seat: Seat) = reservedSeat.add(seat)

    fun getStartTime() = screenTime.startTimeToString()
}
