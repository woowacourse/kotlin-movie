package repository

import model.screening.Screening
import model.screening.Screenings
import java.time.LocalDate
import java.time.LocalDateTime

interface ScreeningRepository {
    fun findByMovieIdAndDate(
        movieId: Long,
        date: LocalDate,
    ): Screenings

    fun findId(
        movieId: Long,
        startDateTime: LocalDateTime,
    ): Long?

    fun findById(screeningId: Long): Screening?
}
