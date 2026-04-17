package movie

import movie.controller.MovieController
import movie.data.MovieData
import movie.data.db.DatabaseInitializer
import movie.data.db.DatabaseManager
import movie.data.db.movie.MovieRepository
import movie.data.db.reservation.ReservationOrderRepository
import movie.domain.movie.Movies
import movie.domain.payment.PriceCalculator
import movie.view.InputView
import movie.view.OutputView

fun main() {
    DatabaseInitializer.initialize()

    val movies =
        DatabaseManager.connection.use { connection ->
            val movieRepository = MovieRepository(connection)
            movieRepository.findAll()
        }

    val controller =
        MovieController(
            inputView = InputView(),
            outputView = OutputView(),
            movies = Movies(movies),
            user = MovieData.createUser(),
            priceCalculator = PriceCalculator(),
            onReservationComplete = { reservations, paymentResult, paymentMethod ->
                DatabaseManager.connection.use { connection ->
                    val repository = ReservationOrderRepository(connection)
                    repository.save(reservations, paymentResult, paymentMethod)
                }
            },
        )
    controller.run()
}
