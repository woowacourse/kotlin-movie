import model.CinemaKiosk
import model.movie.MovieCatalog
import view.InputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
    val movieCatalog: MovieCatalog,
) {
    fun run() {
        // 영화 예매
        if (startReservation().not()) return

        // 결제
    }

    private fun startReservation(): Boolean = InputView.askStartReservation()
}
