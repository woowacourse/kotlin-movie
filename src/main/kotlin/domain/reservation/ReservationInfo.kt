package domain.reservation

import domain.cinema.Showing
import domain.seat.Seats
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

class ReservationInfo(val showing: Showing, val seats: Seats) {

    private val dateTimeFormatter = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        char(':')
        minute()
    }

    override fun toString(): String {
        val time = dateTimeFormatter.format(showing.startTime)
        val seats = seats.label()
        return "- [${showing.movie.title}] $time 좌석: $seats"
    }
}
