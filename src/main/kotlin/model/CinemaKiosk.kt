package model

import model.movie.Movie
import model.movie.MovieName
import model.movie.RunningTime
import model.schedule.CinemaSchedule
import model.seat.SeatColumn
import model.seat.SeatRow

class CinemaKiosk(
    val cinemaSchedule: CinemaSchedule,
) {
    private var reserveResults: MutableList<MovieReservationResult.Success> = mutableListOf()

    fun reserve(
        movieName: MovieName,
        startTime: CinemaTime,
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        val movieSchedule = cinemaSchedule.getMovieSchedule(movieName)
        val movieScreening = movieSchedule.getMovieScreening(startTime)
        val seat = movieScreening.getSeat(seatRow, seatColumn)

        if (reserveResults.any { it.screenTime.overlaps(movieScreening.screenTime) }) {
            return MovieReservationResult.Failed
        }

        if (seat.reserve()) {
            reserveResults.add(
                MovieReservationResult.Success(
                    movie =
                        Movie(
                            name = movieName,
                            runningTime = RunningTime(10),
                        ),
                    screenTime = movieScreening.screenTime,
                    seat = seat,
                ),
            )
            println(reserveResults)
            return MovieReservationResult.Success(
                movie =
                    Movie(
                        name = movieName,
                        runningTime = RunningTime(10),
                    ),
                screenTime = movieScreening.screenTime,
                seat = seat,
            )
        }
        return MovieReservationResult.Failed
    }
}
