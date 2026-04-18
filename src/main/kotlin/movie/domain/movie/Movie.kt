package movie.domain.movie

import movie.domain.screening.Screenings
import java.time.LocalDate

data class Movie(
    val id: Long = 0L,
    val title: MovieTitle,
    val screenings: Screenings,
    val runningTimeMinutes: Int,
) {
    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.hasScreeningOnDate(date)

    fun getScreeningsByDate(date: LocalDate): Screenings = screenings.getScreeningsByDate(date)
}
