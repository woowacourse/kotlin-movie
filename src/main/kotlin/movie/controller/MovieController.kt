package movie.controller

import movie.data.MovieData
import movie.domain.movie.Movie
import movie.domain.reservation.Reservation
import movie.domain.screening.Screening
import movie.domain.user.User
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate

class MovieController(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView(),
    private val movies: List<Movie> = MovieData.createMovies(),
    private val user: User = MovieData.createUser(),
) {
    fun run(){
        if (!askStartReservation()) {
            outputView.endMessage()
            return
        }

    }

    private fun askStartReservation(): Boolean =
        executeWithRetry { inputView.startMessage() }

    private fun <T> executeWithRetry(block: () -> T): T {
        while (true) {
            try {
                return block()
            } catch (e: IllegalArgumentException) {
                outputView.printErrorMessage(e.message.toString())
            }
        }
    }
}
