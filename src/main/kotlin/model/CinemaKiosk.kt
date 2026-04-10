package model

import model.movie.MovieId
import model.schedule.CinemaSchedule
import model.seat.SeatColumn
import model.seat.SeatRow

class CinemaKiosk(
    val cinemaSchedule: CinemaSchedule,
) {
    var reserveResults: MutableList<MovieReservationResult.Success> = mutableListOf()

    fun reserve(
        movieId: MovieId,
        startTime: CinemaTime,
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        val movieSchedule = cinemaSchedule.getMovieSchedule(movieId)
        val movieScreening = movieSchedule.getMovieScreening(startTime)
        val seat = movieScreening.getSeat(seatRow, seatColumn)

        if (reserveResults.any {
                it.screenTime
                    .isEqual(
                        movieScreening.screenTime,
                    ).not() &&
                    it.screenTime.overlaps(movieScreening.screenTime)
            }
        ) {
            return MovieReservationResult.Failed
        }

        if (seat.reserve()) {
            val result =
                MovieReservationResult.Success(
                    movie = movieScreening.movie,
                    screenTime = movieScreening.screenTime,
                    seat = seat,
                )
            reserveResults.add(
                MovieReservationResult.Success(
                    movie = movieScreening.movie,
                    screenTime = movieScreening.screenTime,
                    seat = seat,
                ),
            )
            return result
        }
        return MovieReservationResult.Failed
    }

    fun cancelReservations(
        movieId: MovieId,
        startTime: CinemaTime,
        positions: List<Pair<SeatRow, SeatColumn>>,
    ) {
        val movieSchedule = cinemaSchedule.getMovieSchedule(movieId)
        val movieScreening = movieSchedule.getMovieScreening(startTime)
        positions.forEach { (seatRow, seatColumn) ->
            movieScreening.getSeat(seatRow, seatColumn).cancelReservation()
        }
    }
}
