import domain.Movie
import domain.MovieTheater
import domain.ReservationInfo
import domain.Screen
import domain.Seat
import domain.SeatGrade
import domain.Showing
import domain.User
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
        Seat('A', 1, SeatGrade.S, false),
        Seat('A', 2, SeatGrade.S, false),
        Seat('B', 1, SeatGrade.A, false),
        Seat('B', 2, SeatGrade.A, false),
        Seat('C', 1, SeatGrade.B, false),
        Seat('C', 2, SeatGrade.B, false),
    )

    val screens = listOf(
        Screen(seats.subList(0, 2), 1),
        Screen(seats.subList(2, 4), 2),
        Screen(seats.subList(4, 6), 3),
    )

    val showings = listOf(
        Showing(LocalDateTime(2026, 4, 10, 10, 0), screens[0], movies[0]),
        Showing(LocalDateTime(2026, 4, 10, 14, 0), screens[1], movies[1]),
        Showing(LocalDateTime(2026, 4, 10, 21, 0), screens[2], movies[2]),
    )

    val reservationInfos = listOf(
        ReservationInfo(showings[0], seats[0], users[0]),
        ReservationInfo(showings[1], seats[2], users[1]),
    )

    val movieTheater = MovieTheater(
        screens,
        movies,
        reservationInfos,
        users,
    )
}
