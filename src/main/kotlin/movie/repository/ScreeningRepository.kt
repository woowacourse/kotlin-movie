package movie.repository

import movie.domain.screening.Screening
import movie.domain.screening.Screenings

interface ScreeningRepository {
    fun findById(id: Long): Screening

    fun findAllByMovieId(movieId: Long): Screenings
}
