package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.ScreeningSchedule
import domain.reservation.Cart
import domain.seat.Seat
import kotlinx.datetime.LocalDate
import util.retryOnInvalidInput
import view.InputView
import view.OutputView

class ReservationController(
    val movieTheater: MovieTheater,
) {
    fun run(cart: Cart): Pair<ScreeningSchedule, List<Seat>> {
        val movie = retryOnInvalidInput(OutputView::printError) { chooseMovie() }
        val date = retryOnInvalidInput(OutputView::printError) { chooseDate(movie) }
        val screening = retryOnInvalidInput(OutputView::printError) { chooseScreening(cart, movie, date) }
        val seats = retryOnInvalidInput(OutputView::printError) { chooseSeat(screening) }

        return screening to seats
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        return movieTheater.chooseMovie(input)
    }

    fun chooseDate(movie: Movie): LocalDate {
        val input = InputView.readDate()
        return movieTheater.validateScreeningDate(movie, input)
    }

    fun chooseScreening(
        cart: Cart,
        movie: Movie,
        date: LocalDate,
    ): ScreeningSchedule {
        val screenings = movieTheater.findScreenings(movie, date)

        OutputView.printScreenings(screenings)
        val input = InputView.readScreeningNumber()

        return movieTheater.chooseScreening(cart, movie, date, input)
    }

    fun chooseSeat(screening: ScreeningSchedule): List<Seat> {
        OutputView.printSeats(screening.screen)

        val coordinates = InputView.readSeat()
        return screening.screen.selectSeats(coordinates)
    }
}
