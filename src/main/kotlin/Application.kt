import controller.MovieReservationController
import db.ConnectionManager
import db.DataInitializer
import db.DbScheduler
import db.JdbcMovieRepository
import db.JdbcReservationRepository
import db.JdbcScreeningRepository
import view.InputView
import view.OutputView

fun main() {
    val connection = ConnectionManager.localConnection()
    DataInitializer(connection).initialize()

    val movieRepository = JdbcMovieRepository(connection)
    val screeningRepository = JdbcScreeningRepository(connection)
    val reservationRepository = JdbcReservationRepository(connection, movieRepository, screeningRepository)

    MovieReservationController(
        scheduler = DbScheduler(movieRepository, screeningRepository),
        inputView = InputView,
        outputView = OutputView,
        reservationRepository = reservationRepository,
    ).run()
}
