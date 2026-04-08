package model

import model.seat.Seat
import kotlin.uuid.ExperimentalUuidApi

sealed class MovieReservationResult {
    @OptIn(ExperimentalUuidApi::class)
    data class Success(
        val movie: Movie,
        val screenTime: DateTimeRange,
        val seat: Seat,
    ) : MovieReservationResult()

    object Failed : MovieReservationResult()
}
