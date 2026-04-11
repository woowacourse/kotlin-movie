package movie.domain

class Reservations(
    reservations: List<Reservation> = emptyList(),
) {
    private val _reservations = reservations.toMutableList()

    fun addReservation(reservation: Reservation) {
        require(!isDuplicateTime(reservation = reservation)) { "상영시간은 중복될 수 없습니다." }

        _reservations.add(reservation)
    }

    fun isDuplicateTime(reservation: Reservation): Boolean {
        return _reservations.any {
            it.isDuplicateTime(reservation)
        }
    }
}
