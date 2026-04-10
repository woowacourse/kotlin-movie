import domain.Id
import domain.cart.Cart
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screen
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats
import domain.user.User
import kotlinx.datetime.LocalDateTime

object TestFixtureData {
    val users = listOf(
        User(Id(1)),
        User(Id(2)),
        User(Id(3)),
    )

    val movies = listOf(
        Movie("해리 포터", Id(1), 152),
        Movie("인터스텔라", Id(2), 169),
        Movie("기생충", Id(3), 132),
    )

    val seats = Seats(
        listOf(
            Seat(SeatCoordinate('A', 1), SeatGrade.S, SeatState.RESERVED),
            Seat(SeatCoordinate('A', 2), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 1), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 2), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 1), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 2), SeatGrade.B, SeatState.AVAILABLE),
        ),
    )
    val screens = listOf(
        Screen(seats, Id(1)),
        Screen(seats, Id(2)),
        Screen(seats, Id(3)),
    )

    val showings = listOf(
        Showing(LocalDateTime(2026, 4, 10, 10, 0), screens[0], movies[0]),
        Showing(LocalDateTime(2026, 4, 10, 14, 0), screens[1], movies[1]),
        Showing(LocalDateTime(2026, 4, 10, 21, 0), screens[2], movies[2]),
    )

    val reservationInfos = ReservationInfos(
        listOf(
            ReservationInfo(showings[0], Seats(listOf(seats.seats[0]))),
            ReservationInfo(showings[1], Seats(listOf(seats.seats[2]))),
        ),
    )

    val movieTheater = MovieTheater(
//        screens,
        movies,
        showings,
        reservationInfos,
    )

    val cart: Cart = Cart(
        reservationInfos = reservationInfos,
    )
}
