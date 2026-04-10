package domain.cinema

import domain.reservation.ReservationInfos

class MovieTheater(
//    val screens: List<Screen>,
    val movies: Movies,
    val showings: List<Showing>,
    val reservationInfos: ReservationInfos,
)
