package model.reservation

import model.movie.Movie
import model.seat.Seat
import model.seat.SeatState
import model.time.CinemaTime

data class MovieReservationResult(
    val movie: Movie,
    val startTime: CinemaTime,
    val seat: Seat,
    val state: SeatState,
) {
    fun isEqual(
        movie: Movie,
        startTime: CinemaTime,
        seat: Seat,
    ): Boolean = this.movie == movie && this.startTime == startTime && this.seat == seat
}
