package domain.cinema

import domain.Id
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatState
import util.ErrorMessage

class Screen(
    val seats: List<Seat>,
    val id: Id,
) {
    fun findSeat(
        row: Char,
        column: Int,
    ): Seat? = seats.find { it.coordinate.row == row && it.coordinate.column == column }

    fun findAvailableSeat(coordinate: SeatCoordinate): Seat {
        val seat = findSeat(coordinate.row, coordinate.column)

        require(seat != null) { ErrorMessage.SEAT_NOT_FOUND }
        require(seat.isReserved != SeatState.RESERVED) { ErrorMessage.SEAT_ALREADY_RESERVED }

        return seat
    }

    fun selectSeats(coordinates: List<SeatCoordinate>): List<Seat> = coordinates.map(::findAvailableSeat)

    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
