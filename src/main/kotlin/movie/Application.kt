package movie

import movie.controller.MovieController
import movie.data.MovieData
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
import movie.domain.payment.PriceCalculator
import movie.view.InputView
import movie.view.OutputView

fun main() {
    val controller =
        MovieController(
            inputView = InputView(),
            outputView = OutputView(),
            movies = MovieData.createMovies(),
            user = MovieData.createUser(),
            priceCalculator =
                PriceCalculator(
                    DiscountPolicies(
                        percentagePolicies = listOf(MovieDayDiscount()),
                        fixedPolicies = listOf(TimeDiscount()),
                    ),
                ),
        )
    controller.run()
}
