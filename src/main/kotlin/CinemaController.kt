import model.CinemaKiosk
import model.movie.Movie
import model.movie.MovieCatalog
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
}
