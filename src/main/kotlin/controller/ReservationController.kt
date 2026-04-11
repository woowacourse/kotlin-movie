package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.MovieTime
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.seat.Seats
import view.InputView
import view.OutputView

class ReservationController(val movieTheater: MovieTheater) {
    fun run(): ReservationInfo {
        val movie = chooseMovie()
        val movieTime = chooseDate()
        val showing = chooseShowingTime(movie, movieTime)
        val seats = chooseSeat(showing)

        return ReservationInfo(showing, seats)
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        val movie = movieTheater.movies.findMovieByTitle(input)
        return movie
    }

    fun chooseDate(): MovieTime = InputView.readDate()

    fun chooseShowingTime(
        movie: Movie,
        movieTime: MovieTime,
    ): Showing {
        OutputView.printShowing(
            movieTheater.showings.findByMovieAndDate(movie, movieTime),
        )

        val input = InputView.readShowingNumber()
        return movieTheater.showings.findAvailableShowing(movie, movieTime, input, movieTheater.reservationInfos)
    }

    fun chooseSeat(showing: Showing): Seats {
        OutputView.printSeats(showing.screen)
        return showing.screen.selectSeats(InputView.readSeat())
    }
}
