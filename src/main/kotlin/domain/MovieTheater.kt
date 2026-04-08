
package domain

class MovieTheater(
    val screens: List<Screen>,
    val movies: List<Movie>,
    val showings: List<Showing>,
    val reservationInfos: List<ReservationInfo>,
    val users: List<User>,
)
