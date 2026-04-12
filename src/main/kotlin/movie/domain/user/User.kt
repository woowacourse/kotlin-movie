package movie.domain.user

import movie.domain.amount.Point
import movie.domain.reservation.Reservations

data class User(
    val reservations: Reservations,
    val point: Point,
)
