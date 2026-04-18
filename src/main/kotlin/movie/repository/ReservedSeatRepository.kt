package movie.repository

import movie.domain.seat.Seat

interface ReservedSeatRepository {
    fun findAllByScreeningId(screeningId: Long): List<Seat>

    fun saveAll(
        screeningId: Long,
        seats: List<Seat>,
    )
}
