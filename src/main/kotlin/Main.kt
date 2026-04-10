import controller.Controller
import view.input.InputView
import view.output.OutputView

fun main() {
    Controller(
        inputView = InputView,
        outputView = OutputView,
    ).run()
}
