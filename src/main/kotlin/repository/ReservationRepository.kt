package repository

import model.reservation.Reservations

interface ReservationRepository {
    fun save(reservations: Reservations): Long
}
