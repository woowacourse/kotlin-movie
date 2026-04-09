package domain.reservations.items

import domain.movie.Movie
import domain.seat.Seat
import domain.timetable.items.ScreenTime
import java.time.LocalDate
import java.time.LocalTime

class Reservation(
    private val movie: Movie,
    private val screenTime: ScreenTime,
    private val seats: List<Seat>,
) {
    fun isDuplicatedDate(date: LocalDate): Boolean = screenTime.isScreeningAt(date)

    fun isDuplicatedTime(time: LocalTime): Boolean = screenTime.isContain(time)
}
