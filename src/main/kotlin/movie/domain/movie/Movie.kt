package movie.domain.movie

import movie.domain.screening.Screening
import movie.domain.screening.Screenings
import java.time.LocalDate
import java.util.UUID

data class Movie(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val screenings: Screenings,
) {
    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.hasScreeningOnDate(date)

    fun getScreeningsByDate(date: LocalDate): List<Screening> = screenings.getScreeningsByDate(date)
}
