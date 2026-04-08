package domain.screening

import domain.movie.Movie
import java.time.LocalDate

class Screens(private val value: List<Screen>) {
    fun findScreenings(movie: Movie, date: LocalDate): Screenings {
        val matchingScreenings = value
            .flatMap { it.screenings }
            .filter { it.movie == movie && it.showDate == date }
        return Screenings(matchingScreenings)
    }
}
