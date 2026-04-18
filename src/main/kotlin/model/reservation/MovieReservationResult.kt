package model.reservation

import model.CinemaTimeRange
import model.movie.Movie
import model.seat.Seat

sealed class MovieReservationResult {
    data class Success(
        val movie: Movie,
        val screenTime: CinemaTimeRange,
        val seat: Seat,
    ) : MovieReservationResult()

    object Failed : MovieReservationResult()
}
