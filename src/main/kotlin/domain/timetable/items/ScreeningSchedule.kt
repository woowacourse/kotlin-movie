package domain.timetable.items

import domain.movie.Movie
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.SeatPosition
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

    fun makeReservation(positions: List<SeatPosition>): Reservation {
        val seats = positions.map { screen.findSeat(it) }
        return Reservation(
            movie = movie,
            screenTime = screenTime,
            seats = Seats(seats),
        )
    }
}
