package movie.repository

import movie.model.screening.Screening
import movie.model.seat.Seats

interface ReservedSeatRepository {
    fun save(
        screening: Screening,
        seats: Seats,
    )

    fun findSeatsByScreening(screening: Screening): Seats
}
