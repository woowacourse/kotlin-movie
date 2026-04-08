package domain.user

import domain.amount.Point
import domain.reservation.Reservations

data class User(
    val reservations: Reservations,
    val point: Point
)
