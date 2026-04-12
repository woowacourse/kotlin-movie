package movie.domain.screening

import java.time.LocalDate

class Screenings(
    screenings: List<Screening>,
) {
    private val screenings = screenings.toList()

    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.any { it.screeningDateTime.date == date }

    fun getScreeningsByDate(date: LocalDate): List<Screening> = screenings.filter { it.screeningDateTime.date == date }
}
