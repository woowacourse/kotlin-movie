package model.reservation

import model.screening.Screening

class Reservations(
    reservations: List<Reservation> = emptyList(),
) : Iterable<Reservation> {
    private val value = reservations.toList()

    override fun iterator(): Iterator<Reservation> = value.iterator()

    fun add(newReservation: Reservation): Reservations {
        require(!hasOverlappingWith(newReservation.screening)) { "상영시간이 겹치는 예약은 추가할 수 없습니다." }
        return Reservations(value + newReservation)
    }

    fun hasOverlappingWith(screening: Screening): Boolean =
        value.any { it.screening.hasOverlappingWith(screening) }

    fun isEmpty(): Boolean = value.isEmpty()
}
