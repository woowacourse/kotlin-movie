package model.seat

import api.exception.SeatAlreadyReservedException

data class Seat(
    val row: String,
    val column: Int,
    val isReserved: Boolean = false,
    val seatRank: SeatRank,
) {
    fun getSeatName(): String = "$row$column"

    fun reserve(): Seat {
        if (isReserved) throw SeatAlreadyReservedException()
        return copy(
            isReserved = true,
        )
    }
}
