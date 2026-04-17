package movie.domain.movie

import movie.domain.screening.Screening
import movie.domain.screening.Screenings
import java.time.LocalDate

data class Movie(
    val id: Long,
    val title: String,
    val screenings: Screenings,
) {
    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.hasScreeningOnDate(date)

    fun getScreeningsByDate(date: LocalDate): List<Screening> = screenings.getScreeningsByDate(date)
}
