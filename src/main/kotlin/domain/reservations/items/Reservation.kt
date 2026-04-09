package domain.reservations.items

import domain.money.Money
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

    fun getReservationInfo(): ReservationInfo {
        val price = seats.sumOf { it.getPrice().getAmount() }

        return ReservationInfo(
            screenTime = screenTime,
            price = Money(price),
        )
    }

    fun getReservationSummary(): String {
        val title = movie.getTitle()
        val date = screenTime.screeningDateToString()
        val time = screenTime.startTimeToString()
        val seatNumbers = seats.joinToString(separator = ", ") { it.getSeatNumber() }
        return "[$title] $date $time 좌석: $seatNumbers"
    }
}
