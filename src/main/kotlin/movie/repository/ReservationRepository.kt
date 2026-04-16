package movie.repository

import movie.model.reservation.Reservation
import movie.model.reservation.Reservations
import java.time.LocalDate

interface ReservationRepository {
    fun save(reservation: Reservation)

    fun findByDate(date: LocalDate): Reservations
}
