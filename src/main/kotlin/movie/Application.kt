package movie

import movie.controller.MovieController
import movie.data.MovieData
import movie.domain.movie.Movies
import movie.domain.payment.PriceCalculator
import movie.view.InputView
import movie.view.OutputView

fun main() {
    val controller =
        MovieController(
            inputView = InputView(),
            outputView = OutputView(),
            movies = Movies(MovieData.createMovies()),
            user = MovieData.createUser(),
            priceCalculator = PriceCalculator(),
        )
    controller.run()
}
