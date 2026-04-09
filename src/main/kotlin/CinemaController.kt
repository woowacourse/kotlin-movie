import model.CinemaTime
import model.movie.MovieName
import model.schedule.CinemaSchedule
import model.schedule.MovieNameGroup
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaSchedule: CinemaSchedule,
) {
    val movieNames =
        MovieNameGroup(
            listOf(
                MovieName("혼자사는남자", id = "1"),
            ),
        )

    fun run() {
        if (startMovieReservation().not()) return
        val movieSchedule = getMovieSchedule()
        val date = inputDate()
        OutputView.showMovieSchedule(movieSchedule)
        val movieScreening = selectMovieScreening(movieSchedule)
    }

    private fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation(Message.START_RESERVATION)
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getMovieSchedule(): MovieSchedule {
        while (true) {
            val movieName = movieNames.find(InputView.inputMovieName())
            if (movieName != null) {
                val movieSchedule = cinemaSchedule.getMovieSchedule(movieName)
                if (movieSchedule.isEmpty().not()) return movieSchedule
            }
        }
    }

    private fun inputDate(): CinemaTime {
        while (true) {
            try {
                return InputView.inputDate()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun selectMovieScreening(movieSchedule: MovieSchedule): MovieScreening {
        while (true) {
            try {
                return InputView.selectMovieScreening(movieSchedule)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}
