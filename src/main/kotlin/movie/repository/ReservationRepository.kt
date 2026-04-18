package movie.repository

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.reservation.Reservations

interface ReservationRepository {
    fun save(
        reservations: Reservations,
        totalPrice: Money,
        usedPoints: Point,
        paymentMethod: String,
    ): Long
}
