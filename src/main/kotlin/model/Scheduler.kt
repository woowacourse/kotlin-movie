package model

import model.movie.Movie
import model.screening.Screening
import model.screening.Screenings
import repository.ScreeningRepository
import java.time.LocalDate

class Scheduler(
    private val screeningRepository: ScreeningRepository,
) {
    fun update(newScreening: Screening) = screeningRepository.update(newScreening)

    fun findBy(
        movie: Movie,
        date: LocalDate,
    ): Screenings = screeningRepository.findBy(movie, date)
}
