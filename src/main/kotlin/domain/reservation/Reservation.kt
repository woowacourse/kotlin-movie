package domain.reservation

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.seat.Seat
import kotlinx.datetime.LocalDate

object Reservation {
    fun findMovieById(
        movieTheater: MovieTheater,
        id: Int,
    ): Movie {
        val movieIndex = movieTheater.movies.indexOfFirst { it.id == id }
        require(movieIndex != -1) { "해당 영화는 존재하지 않습니다." }
        return movieTheater.movies[movieIndex]
    }

    fun findShowing(
        movieTheater: MovieTheater,
        movie: Movie,
        date: LocalDate,
    ): List<Showing> {
        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        require(showings.isNotEmpty()) { "해당 영화는 해당 날짜에 상영되지 않습니다." }
        return showings
    }

    fun makeReservation(
        movieTheater: MovieTheater,
        showing: Showing,
    ) {
    }

    fun checkReservationHistory(
        reservationInfos: List<ReservationInfo>,
        showing: Showing,
    ) {
        val history = reservationInfos.filter {
            showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
        }

        require(history.isEmpty()) { "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요." }
    }

    fun checkSeatFormat(seat: String) {
        require(Regex("^[A-Z][0-9]+$").matches(seat)) { "입력된 값이 유효하지 않습니다." }
    }

    fun checkSeat(
        seats: List<Seat>,
        input: String,
    ): Seat {
        checkSeatFormat(input)
        val row = input[0]
        val column = input.substring(1).toInt()

        val seat = seats.filter { it.row == row && it.column == column }
        require(seat.isNotEmpty()) { "해당 상영관에는 해당 좌석이 존재하지 않습니다." }
        require(!seat.first().isReserved) { "해당 좌석은 이미 예약되었습니다." }

        return seat.first()
    }
}
