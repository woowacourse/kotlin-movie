package movie.console

import movie.console.controller.MovieReservationController
import movie.console.view.InputView
import movie.console.view.OutputView
import movie.database.DatabaseInitializer
import movie.repository.inmemory.InMemoryScreenRepository
import movie.repository.jdbc.JdbcMovieRepository
import movie.repository.jdbc.JdbcReservationRepository
import movie.repository.jdbc.JdbcReservedSeatRepository
import movie.repository.jdbc.JdbcScreeningRepository

fun main() {
    val isLocal = true

    DatabaseInitializer.initTables(isLocal)

    val inputView = InputView
    val outputView = OutputView

    val movieRepository = JdbcMovieRepository(isLocal)
    val screenRepository = InMemoryScreenRepository
    val screeningRepository =
        JdbcScreeningRepository(
            isLocal = isLocal,
            screenRepository = screenRepository,
            movieRepository = movieRepository,
        )
    val reservationRepository = JdbcReservationRepository(isLocal, screeningRepository)
    val reservedSeatRepository = JdbcReservedSeatRepository(isLocal)

    MovieReservationController(
        screeningRepository = screeningRepository,
        movieRepository = movieRepository,
        reservationRepository = reservationRepository,
        reservedSeatRepository = reservedSeatRepository,
        inputView = inputView,
        outputView = outputView,
    ).run()
}
