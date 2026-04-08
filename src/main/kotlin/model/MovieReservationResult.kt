package model

import model.movie.Movie
import model.seat.Seat

sealed class MovieReservationResult {
    data class Success(
        val movie: Movie,
        val screenTime: DateTimeRange,
        val seat: Seat,
    ) : MovieReservationResult()

    object Failed : MovieReservationResult()
}
