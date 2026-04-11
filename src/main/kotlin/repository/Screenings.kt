package repository

import domain.screening.Screening
import java.time.LocalDate

class Screenings(
    val screenings: List<Screening>,
) {
    fun findByMovieTitleAndDate(
        title: String,
        date: LocalDate,
    ): List<Screening> =
        screenings
            .filter {
                it.movie.title.value == title && it.startTime.value.toLocalDate() == date
            }.sortedBy { it.startTime.value }

    fun updateScreening(updatedScreening: List<Screening>): Screenings = Screenings(updatedScreening)
}
