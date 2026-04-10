import model.CinemaKiosk
import model.CinemaTime
import model.movie.Movie
import model.movie.MovieCatalog
import model.schedule.MovieScreening
import model.schedule.onDate
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
    val movieCatalog: MovieCatalog,
) {
    fun run() {
        // 영화 예매
        if (startReservation().not()) return
        val selectedMovie = selectMovie(movieCatalog)
        val movieSchedule = getMovieSchedule(selectedMovie)
        val selectedDate = selectDate()
        val onDateMovieSchedule = movieSchedule.onDate(selectedDate)
        OutputView.showMovieScreenings(onDateMovieSchedule)
        // 결제
    }

    private fun startReservation(): Boolean = InputView.askStartReservation()

    private fun selectMovie(movieCatalog: MovieCatalog): Movie {
        while (true) {
            val input = InputView.inputMovieName()
            val movie = movieCatalog.findByName(input)
            if (movie != null) return movie
            OutputView.showInvalidMovieName()
        }
    }

    private fun getMovieSchedule(movie: Movie): List<MovieScreening> = cinemaKiosk.cinemaSchedule.getMovieScreenings(movie.id)

    private fun selectDate(): CinemaTime {
        while (true) {
            try {
                val input = InputView.inputDate()
                return input
            } catch (e: Exception) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }
}
