package domain.reservation

import domain.cinema.Showing
import domain.seat.Seat

class ReservationInfo(val showing: Showing, val seat: Seat) {
    companion object {
        fun create(
            showing: Showing,
            seat: Seat,
        ): ReservationInfo {
            return ReservationInfo(
                showing,
                seat,
            )
        }
    }
}
