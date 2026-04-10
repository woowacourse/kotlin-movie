package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.seat.Seats
import kotlinx.datetime.LocalDate
import view.InputView
import view.OutputView

class ReservationController(val movieTheater: MovieTheater) {
    fun run(): ReservationInfo {
        val movie = chooseMovie()
        val date = chooseDate(movie)
        val showing = chooseShowingTime(movie, date)
        val seats = chooseSeat(showing)

        return ReservationInfo(showing, seats)
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        val movie = movieTheater.movies.findMovieByTitle(input)
        return movie
    }

    fun chooseDate(movie: Movie): LocalDate {
        val input = InputView.readDate()
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)" }

        return date
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.showings.findByMovieAndDate(movie, date)

        OutputView.printShowing(showings)

        val input = InputView.readShowingNumber()
        val selected = showings.findByIndex(input)

        movieTheater.reservationInfos.checkReservationHistory(selected)

        return selected
    }

    fun chooseSeat(showing: Showing): Seats {
        OutputView.printSeats(showing.screen)

        val input = InputView.readSeat()

        val seatInputs =
            input.split(',')
                .map { it.trim() }

        val seats = Seats(
            seatInputs.map { seatInput ->
                showing.screen.seats.checkSeat(seatInput)
            },
        )
        return seats
    }
}
