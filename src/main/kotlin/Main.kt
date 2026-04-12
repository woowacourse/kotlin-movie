import controller.BookingController
import controller.CartController
import controller.PaymentController
import controller.ReservationController
import domain.Id
import domain.user.User

fun main() {
    val user = User(Id(1))

    BookingController(
        reservationController = ReservationController(MockData.movieTheater),
        cartController = CartController(),
        paymentController = PaymentController(),
        user = user,
    ).run()
}
