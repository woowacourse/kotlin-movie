package domain.reservation

import domain.seat.Seat
import domain.seat.SeatState
import domain.seat.Seats

object Reservation {
    fun checkSeatFormat(seat: String) {
        require(Regex("^[A-Z][0-9]+$").matches(seat)) { "입력된 값이 유효하지 않습니다." }
    }

    fun checkSeat(
        seats: Seats,
        input: String,
    ): Seat {
        checkSeatFormat(input)
        val row = input[0]
        val column = input.substring(1).toInt()

        val seat = seats.seats.filter { it.coordinate.row == row && it.coordinate.column == column }
        require(seat.isNotEmpty()) { "해당 상영관에는 해당 좌석이 존재하지 않습니다." }
        require(seat.first().isReserved != SeatState.RESERVED) { "해당 좌석은 이미 예약되었습니다." }

        return seat.first()
    }
}
