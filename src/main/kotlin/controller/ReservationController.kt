package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.Reservation
import domain.seat.Seats
import kotlinx.datetime.LocalDate
import view.InputView
import view.OutputView

class ReservationController(val movieTheater: MovieTheater) {
    fun run(): Pair<Showing, Seats> {
        val movie = chooseMovie()
        val date = chooseDate(movie)
        val showing = chooseShowingTime(movie, date)
        val seats = chooseSeat(showing)

        return showing to seats
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

        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        require(showings.isNotEmpty()) { "해당 날짜에 선택한 영화의 상영이 없습니다." }

        return date
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }

        OutputView.printShowing(showings)
        val input = InputView.readShowingNumber()

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }

        Reservation.checkReservationHistory(movieTheater.reservationInfos, showings[input.toInt() - 1])

        return showings[input.toInt() - 1]
    }

    fun chooseSeat(showing: Showing): Seats {

        val screen = showing.screen

        OutputView.printSeats(screen)

        val input = InputView.readSeat()

        val seatInputs = input.split(',').map { it.trim() }

        val seats = Seats(
            seatInputs.map { seat ->
                Reservation.checkSeat(screen.seats, seat)
            },
        )
        return seats
    }
}
