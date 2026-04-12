package model

import model.schedule.CinemaSchedule
import model.schedule.MovieScreening
import model.seat.SeatColumn
import model.seat.SeatRow

class CinemaKiosk(
    val cinemaSchedule: CinemaSchedule,
) {
    val reserveResults: MutableList<MovieReservationResult.Success> = mutableListOf()

    fun reserve(
        movieScreening: MovieScreening,
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        val seat = movieScreening.getSeat(seatRow, seatColumn)

        if (reserveResults.any {
                it.screenTime != movieScreening.screenTime &&
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
            reserveResults.add(result)
            return result
        }
        return MovieReservationResult.Failed
    }

    fun cancelReservations(
        movieScreening: MovieScreening,
        positions: List<Pair<SeatRow, SeatColumn>>,
    ) {
        positions.forEach { (seatRow, seatColumn) ->
            movieScreening.getSeat(seatRow, seatColumn)?.cancelReservation()
        }
    }

    fun reserveSeats(
        movieScreening: MovieScreening,
        selectedSeats: List<Pair<SeatRow, SeatColumn>>,
    ): List<MovieReservationResult.Success> {
        selectedSeats.forEach { (seatRow, seatColumn) -> movieScreening.getSeat(seatRow, seatColumn) }
        val success = mutableListOf<MovieReservationResult.Success>()
        for ((row, col) in selectedSeats) {
            when (
                val result =
                    reserve(
                        movieScreening = movieScreening,
                        seatRow = row,
                        seatColumn = col,
                    )
            ) {
                is MovieReservationResult.Success -> success.add(result)
                is MovieReservationResult.Failed -> {
                    cancelReservations(movieScreening, success.map { it.seat.row to it.seat.column })
                    throw IllegalArgumentException(Message.INVALID_SEAT)
                }
            }
        }
        return success
    }
}
