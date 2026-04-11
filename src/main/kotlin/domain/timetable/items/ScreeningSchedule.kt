package domain.timetable.items

import domain.movie.Movie
import domain.movie.itmes.Title
import domain.seat.Seat
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screen: Screen,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: ReservedSeats = ReservedSeats()

    fun isScreeningMovieTitle(title: Title) = movie.isValidTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun isReservedSeat(seat: Seat) = reservedSeat.isReserved(seat)

    fun addReserveSeat(seat: Seat) = reservedSeat.addSeat(seat)
}
