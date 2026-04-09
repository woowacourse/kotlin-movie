import controller.CartController
import controller.PaymentController
import controller.ReservationController
import domain.Cart
import domain.Movie
import domain.MovieTheater
import domain.Screen
import domain.Seat
import domain.SeatGrade
import domain.Showing
import domain.User
import kotlinx.datetime.LocalDateTime

fun main() {
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

    val movieTheater = MovieTheater(
        screens,
        movies,
        showings,
        emptyList(),
    )

    lateinit var cart: Cart
    val cartController = CartController()
    while (true) {
        val reservationController = ReservationController(
            movieTheater = movieTheater,
        )
        val pair = reservationController.run()

        cart = cartController.run(
            showing = pair.first,
            seats = pair.second,
        )

        println("다른 영화를 추가하시겠습니까? (Y/N)")
        val input = readln()
        if (input != "Y") break
    }

    val paymentController = PaymentController(
        cart = cart,
        user = User(1),
    )

    paymentController.run()
}
