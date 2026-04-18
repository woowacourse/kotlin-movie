package movie.domain

import movie.error.ScheduleErrorMessage
import movie.domain.seat.SeatNumber
import kotlin.collections.toMutableList

class Cart(
    reservations: List<Reservation> = emptyList(),
) {
    private val _reservations = reservations.toMutableList()

    fun getReservations(): List<Reservation> = _reservations.toList()

    fun addReservation(
        schedule: Schedule,
        seats: List<SeatNumber>,
    ) {
        require(!isDuplicateTime(schedule = schedule)) { ScheduleErrorMessage.DUPLICATE_TIME }

        _reservations.add(Reservation(schedule, seats))
    }

    fun isDuplicateTime(schedule: Schedule): Boolean =
        _reservations.any {
            it.isDuplicateTime(schedule)
        }
}
