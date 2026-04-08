package domain.reservation

import domain.screening.Screening

class Reservations(private val value: List<Reservation> = emptyList()) : Iterable<Reservation> {
    override fun iterator(): Iterator<Reservation> = value.iterator()

    fun addReservation(reservation: Reservation): Reservations {
        require(!isOverlapping(reservation.screening)) { "상영시간이 겹치는 예약은 추가할 수 없습니다." }
        return Reservations(value + reservation)
    }

    fun isOverlapping(screening: Screening): Boolean =
        value.any { it.screening.isOverlapping(screening) }

    fun isEmpty(): Boolean = value.isEmpty()
}
