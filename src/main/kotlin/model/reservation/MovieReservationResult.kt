package model.reservation

import model.movie.Movie
import model.schedule.MovieScreening
import model.seat.Seat
import model.seat.SeatState
import model.time.CinemaTimeRange

data class MovieReservationResult(
    val movie: Movie,
    val screenTime: CinemaTimeRange,
    val seat: Seat,
    private val state: SeatState,
) {
    fun isEqual(
        movie: Movie,
        startTime: CinemaTimeRange,
        seat: Seat,
    ): Boolean = this.movie == movie && this.screenTime == startTime && this.seat == seat

    fun isEqual(movieScreening: MovieScreening): Boolean = movieScreening.movie == movie && movieScreening.screenTime == screenTime
}
