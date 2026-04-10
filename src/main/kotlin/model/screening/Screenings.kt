package model.screening

import model.movie.Movie
import java.time.LocalDate

class Screenings(
    private val value: List<Screening>,
) : Iterable<Screening> {
    override fun iterator(): Iterator<Screening> = value.iterator()

    fun isEmpty(): Boolean = value.isEmpty()

    fun findBy(
        movie: Movie,
        date: LocalDate,
    ): Screenings = Screenings(value.filter { it.movie == movie && it.showDate == date })

    fun update(newScreening: Screening): Screenings {
        val updatedList =
            value.map {
                if (it.isSameScreening(newScreening)) newScreening else it
            }
        return Screenings(updatedList)
    }
}
