package model

import model.movie.Movie
import model.schedule.CinemaSchedule
import model.seat.SeatColumn
import model.seat.SeatRow

class CinemaKiosk(
    val cinemaSchedule: CinemaSchedule,
) {
    private var reserveResults: MutableList<MovieReservationResult.Success> = mutableListOf()

    fun reserve(
        movie: Movie,
        startTime: CinemaTime,
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        val movieSchedule = cinemaSchedule.getMovieSchedule(movie)
        val movieScreening = movieSchedule.getMovieScreening(startTime)
        val seat = movieScreening.getSeat(seatRow, seatColumn)

        if (reserveResults.any { it.screenTime.overlaps(movieScreening.screenTime) }) {
            return MovieReservationResult.Failed
        }

        if (seat.reserve()) {
            reserveResults.add(
                MovieReservationResult.Success(
                    movie = movie,
                    screenTime = movieScreening.screenTime,
                    seat = seat,
                ),
            )
            println(reserveResults)
            return MovieReservationResult.Success(
                movie = movie,
                screenTime = movieScreening.screenTime,
                seat = seat,
            )
        }
        return MovieReservationResult.Failed
    }
}
