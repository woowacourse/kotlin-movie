import controller.CartController
import controller.FlowController
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
import view.InputView

fun main() {
    val movies = listOf(
        Movie("F1 더 무비", 1, 130),
        Movie("토이 스토리", 2, 100),
        Movie("아이언맨", 3, 126),
    )

    val seats = listOf(
        Seat('A', 1, SeatGrade.B, false),
        Seat('A', 2, SeatGrade.B, false),
        Seat('A', 3, SeatGrade.B, false),
        Seat('A', 4, SeatGrade.B, false),
        Seat('B', 1, SeatGrade.B, false),
        Seat('B', 2, SeatGrade.B, false),
        Seat('B', 3, SeatGrade.B, false),
        Seat('B', 4, SeatGrade.B, false),
        Seat('C', 1, SeatGrade.S, false),
        Seat('C', 2, SeatGrade.S, false),
        Seat('C', 3, SeatGrade.S, false),
        Seat('C', 4, SeatGrade.S, false),
        Seat('D', 1, SeatGrade.S, false),
        Seat('D', 2, SeatGrade.S, false),
        Seat('D', 3, SeatGrade.S, false),
        Seat('D', 4, SeatGrade.S, false),
        Seat('E', 1, SeatGrade.A, false),
        Seat('E', 2, SeatGrade.A, false),
        Seat('E', 3, SeatGrade.A, false),
        Seat('E', 4, SeatGrade.A, false),
    )

    val screens = listOf(
        Screen(seats, 1),
        Screen(seats, 2),
        Screen(seats, 3),
    )

    val showings = listOf(
        Showing(LocalDateTime(2025, 9, 20, 10, 20), screens[0], movies[0]),
        Showing(LocalDateTime(2025, 9, 20, 13, 0), screens[0], movies[0]),
        Showing(LocalDateTime(2025, 9, 20, 15, 40), screens[0], movies[0]),
        Showing(LocalDateTime(2025, 9, 20, 20, 10), screens[0], movies[0]),
        Showing(LocalDateTime(2025, 9, 20, 13, 30), screens[1], movies[1]),
        Showing(LocalDateTime(2025, 9, 20, 16, 0), screens[1], movies[1]),
        Showing(LocalDateTime(2025, 9, 20, 9, 50), screens[2], movies[2]),
    )

    val movieTheater = MovieTheater(
        screens,
        movies,
        showings,
        emptyList(),
    )

    lateinit var cart: Cart
    val cartController = CartController()
    val flowController = FlowController()

    var input = InputView.startTicketing()
    while (flowController.start(input)) {
        val reservationController = ReservationController(
            movieTheater = movieTheater,
        )
        val pair = reservationController.run()

        cart = cartController.run(
            showing = pair.first,
            seats = pair.second,
        )

        input = InputView.continueTicketing()
    }

    val paymentController = PaymentController(
        cart = cart,
        user = User(1),
    )

    paymentController.run()
}
