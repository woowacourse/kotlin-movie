package domain.cinema

import domain.Id
import domain.seat.Seats

class Screen(val seats: Seats, val id: Id) {
    fun selectSeats(input: String): Seats {
        val inputs = input.split(',').map { it.trim() }
        return Seats(inputs.map { seats.checkSeat(it) })
    }

    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
