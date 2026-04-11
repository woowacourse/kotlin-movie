package domain.reservations

import domain.reservations.items.Reservation

class Reservations(
    private val reservations: MutableList<Reservation> = mutableListOf(),
) {
    fun addReservation(reservation: Reservation) {
        val duplicated = reservations.any { it.isDuplicatedReservation(reservation) }

        require(!duplicated) { "선택하신 상영 시간이 겹칩니다." }

        reservations.add(reservation)
    }
}
