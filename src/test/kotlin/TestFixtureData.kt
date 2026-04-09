import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screen
import domain.cinema.Showing
import domain.reservation.Cart
import domain.reservation.ReservationInfo
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.user.User
import kotlinx.datetime.LocalDateTime

object TestFixtureData {
    val users = listOf(
        User(1),
        User(2),
        User(3),
    )

    val movies = listOf(
        Movie("해리 포터", 1, 152),
        Movie("인터스텔라", 2, 169),
        Movie("기생충", 3, 132),
    )

    val seats = listOf(
        Seat('A', 1, SeatGrade.S, true),
        Seat('A', 2, SeatGrade.S, false),
        Seat('B', 1, SeatGrade.A, false),
        Seat('B', 2, SeatGrade.A, false),
        Seat('C', 1, SeatGrade.B, false),
        Seat('C', 2, SeatGrade.B, false),
    )

    val screens = listOf(
        Screen(seats, 1),
        Screen(seats, 2),
        Screen(seats, 3),
    )

    val showings = listOf(
        Showing(LocalDateTime(2026, 4, 10, 10, 0), screens[0], movies[0]),
        Showing(LocalDateTime(2026, 4, 10, 14, 0), screens[1], movies[1]),
        Showing(LocalDateTime(2026, 4, 10, 21, 0), screens[2], movies[2]),
    )

    val reservationInfos = listOf(
        ReservationInfo(showings[0], seats[0]),
        ReservationInfo(showings[1], seats[2]),
    )

    val movieTheater = MovieTheater(
        screens,
        movies,
        showings,
        reservationInfos,
    )

    val cart: Cart = Cart(
        reservationInfos = reservationInfos,
    )
}
