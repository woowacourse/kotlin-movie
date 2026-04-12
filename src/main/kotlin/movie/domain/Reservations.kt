package movie.domain

import movie.domain.seat.SeatNumber

class Reservations(
    reservations: List<Reservation> = emptyList(),
) {
    private val _reservations = reservations.toMutableList()

    fun getReservations(): List<Reservation> = _reservations.toList()

    fun addReservation(
        schedule: Schedule,
        seats: List<SeatNumber>,
    ) {
        require(!isDuplicateTime(schedule = schedule)) { "상영시간은 중복될 수 없습니다." }

        _reservations.add(Reservation(schedule, seats))
    }

    fun isDuplicateTime(schedule: Schedule): Boolean =
        _reservations.any {
            it.isDuplicateTime(schedule)
        }
}
