package movie.service

import movie.domain.Reservation
import movie.repository.ReservationRepository

class ReservationService(
    private val reservationRepository: ReservationRepository
) {
    fun saveReservation(reservation: Reservation): Long {
        return reservationRepository.save(reservation)
    }
}
