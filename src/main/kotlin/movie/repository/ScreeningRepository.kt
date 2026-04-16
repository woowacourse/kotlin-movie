package movie.repository

import movie.model.movie.Movie
import movie.model.screening.Screening
import movie.model.screening.Screenings
import java.time.LocalDate

interface ScreeningRepository {
    fun findBy(
        movie: Movie,
        date: LocalDate,
    ): Screenings

    fun findById(id: Long): Screening?

    fun getAllScreenings(): Screenings
}
