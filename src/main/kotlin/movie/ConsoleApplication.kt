package movie

import movie.controller.MovieController
import movie.infrastructure.db.DatabaseConnector
import movie.infrastructure.db.DatabaseInitializer
import movie.infrastructure.db.JdbcMovieRepository
import movie.infrastructure.db.JdbcReservationRepository
import movie.infrastructure.db.JdbcReservedSeatRepository
import movie.infrastructure.db.JdbcScreeningRepository

fun main() {
    val connection = DatabaseConnector.connectLocal()
    DatabaseInitializer(connection).initialize()

    val reservedSeatRepository = JdbcReservedSeatRepository(connection)
    val screeningRepository = JdbcScreeningRepository(connection, reservedSeatRepository)
    val movieRepository = JdbcMovieRepository(connection, screeningRepository)
    val reservationRepository = JdbcReservationRepository(connection, reservedSeatRepository)

    val controller =
        MovieController(
            movieRepository = movieRepository,
            reservationRepository = reservationRepository,
        )
    controller.run()
}
