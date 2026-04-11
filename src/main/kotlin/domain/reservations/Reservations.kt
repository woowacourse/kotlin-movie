package domain.reservations

import domain.reservations.items.Reservation

class Reservations(
    private val reservations: List<Reservation> = mutableListOf(),
)
