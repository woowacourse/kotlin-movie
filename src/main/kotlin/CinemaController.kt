import model.movie.MovieName
import model.schedule.CinemaSchedule
import model.schedule.MovieNameGroup
import model.schedule.MovieSchedule
import view.InputView

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
    }

    fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation(Message.START_RESERVATION)
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun getMovieSchedule(): MovieSchedule {
        while (true) {
            val movieName = movieNames.find(InputView.inputMovieName())
            if (movieName != null) {
                val movieSchedule = cinemaSchedule.getMovieSchedule(movieName)
                if (movieSchedule.isEmpty().not()) return movieSchedule
            }
        }
    }
}
