import controller.MovieReservationController
import repository.ScreeningRepository
import view.InputView
import view.OutputView

fun main() {
    MovieReservationController(
        screeningRepo = ScreeningRepository(),
        inputView = InputView,
        outputView = OutputView,
    ).run()
}
