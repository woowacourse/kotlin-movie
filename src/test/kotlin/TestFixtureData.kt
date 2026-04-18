import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screen
import domain.cinema.ScreeningSchedule
import domain.reservation.Cart
import domain.reservation.ReservationInfo
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.user.User
import kotlinx.datetime.LocalDateTime

object TestFixtureData {
    val users =
        listOf(
            User(Id("user-1")),
            User(Id("user-2")),
            User(Id("user-3")),
        )

    val movies =
        listOf(
            Movie("해리 포터", Id("movie-harry-potter"), 152),
            Movie("인터스텔라", Id("movie-interstellar"), 169),
            Movie("기생충", Id("movie-parasite"), 132),
        )

    val seats =
        listOf(
            Seat(SeatCoordinate('A', 1), SeatGrade.S, SeatState.RESERVED),
            Seat(SeatCoordinate('A', 2), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 1), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 2), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 1), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 2), SeatGrade.B, SeatState.AVAILABLE),
        )

    val screens =
        listOf(
            Screen(seats, Id("screen-1")),
            Screen(seats, Id("screen-2")),
            Screen(seats, Id("screen-3")),
        )

    val screenings =
        listOf(
            ScreeningSchedule(LocalDateTime(2026, 4, 10, 10, 0), screens[0], movies[0]),
            ScreeningSchedule(LocalDateTime(2026, 4, 10, 14, 0), screens[1], movies[1]),
            ScreeningSchedule(LocalDateTime(2026, 4, 10, 21, 0), screens[2], movies[2]),
        )

    val reservationInfos =
        listOf(
            ReservationInfo(screenings[0], seats[0]),
            ReservationInfo(screenings[1], seats[2]),
        )

    val movieTheater =
        MovieTheater(
            screens,
            movies,
            screenings,
        )

    val cart: Cart =
        Cart(
            reservationInfos = reservationInfos,
        )
}
