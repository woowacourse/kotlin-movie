package domain.cinema

import domain.reservation.ReservationInfo

class MovieTheater(
//    val screens: List<Screen>,
    val movies: List<Movie>,
    val showings: List<Showing>,
    val reservationInfos: List<ReservationInfo>,
)
