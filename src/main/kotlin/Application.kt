import controller.MovieReservationController
import model.Scheduler
import repository.ScreeningRepository
import view.InputView
import view.OutputView

fun main() {
    MovieReservationController(
        scheduler = Scheduler(ScreeningRepository()),
        inputView = InputView,
        outputView = OutputView,
    ).run()
}
