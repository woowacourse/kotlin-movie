package model

import model.reservation.MovieReservationResult
import model.reservation.Reservations
import model.schedule.MovieScreening
import model.seat.SeatColumn
import model.seat.SeatRow

class CinemaKiosk {
    private val reservations = Reservations()
    val reserveResults: List<MovieReservationResult.Success> get() = reservations.all

    fun reserve(
        movieScreening: MovieScreening,
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        if (!reservations.canAccept(movieScreening)) {
            return MovieReservationResult.Failed
        }
        val result = movieScreening.reserve(seatRow, seatColumn)
        if (result is MovieReservationResult.Success) {
            reservations.add(result)
        }
        return result
    }

    fun cancelReservations(
        movieScreening: MovieScreening,
        positions: List<Pair<SeatRow, SeatColumn>>,
    ) {
        positions.forEach { (seatRow, seatColumn) ->
            movieScreening.cancel(seatRow, seatColumn)
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
