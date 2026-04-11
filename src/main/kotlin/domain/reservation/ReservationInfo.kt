package domain.reservation

import domain.cinema.Showing
import domain.seat.Seats

class ReservationInfo(val showing: Showing, val seats: Seats) {

    override fun toString(): String {
        val time = showing.startTime.format()
        val seats = seats.label()
        return "- [${showing.movie.title}] $time 좌석: $seats"
    }
}
