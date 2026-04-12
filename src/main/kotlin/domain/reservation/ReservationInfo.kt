package domain.reservation

import domain.cinema.Showing
import domain.seat.Seats

class ReservationInfo(val showing: Showing, val seats: Seats)
