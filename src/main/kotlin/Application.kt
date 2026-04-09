import controller.MovieReservationController
import model.Scheduler
import view.InputView
import view.OutputView

fun main() {
    MovieReservationController(
        scheduler = Scheduler(),
        inputView = InputView,
        outputView = OutputView,
    ).run()
}
