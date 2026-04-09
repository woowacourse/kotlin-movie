package domain.cinema

import domain.Id
import domain.seat.Seat

class Screen(val seats: List<Seat>, val id: Id) {
    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
