package domain.timetable.items

import domain.movie.Movie
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.seat.items.SeatPosition
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: MutableList<Seat> = mutableListOf()

    fun isScreeningMovieTitle(title: Title) = movie.isValidTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun isReservedSeat(seatNumber: SeatPosition) = reservedSeat.any { it.isExistSeatPosition(seatNumber) }

    fun reserveSeat(seat: Seat) = reservedSeat.add(seat)
}
