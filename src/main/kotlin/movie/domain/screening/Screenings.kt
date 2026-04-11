package movie.domain.screening

import java.time.LocalDate

data class Screenings(
    private val screenings: List<Screening>,
) {
    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.any { it.slot.date == date }

    fun getScreeningsByDate(date: LocalDate): List<Screening> = screenings.filter { it.slot.date == date }
}
