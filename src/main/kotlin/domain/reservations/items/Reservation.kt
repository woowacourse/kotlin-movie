package domain.reservations.items

import domain.money.Money
import domain.movie.Movie
import domain.timetable.items.ScreenTime
import domain.timetable.items.Seats
import java.time.LocalDate
import java.time.LocalTime

class Reservation(
    private val movie: Movie,
    private val screenTime: ScreenTime,
    private val seats: Seats,
) {
    fun isDuplicatedDate(date: LocalDate): Boolean = screenTime.isScreeningAt(date)

    fun isDuplicatedTime(time: LocalTime): Boolean = screenTime.isContainsTime(time)

    fun isDuplicatedReservation(otherReservation: Reservation): Boolean =
        this.screenTime.isDuplicatedScreenTime(otherReservation.screenTime)

    fun sumSeatPrice(): Money = seats.sumPrice()
}
