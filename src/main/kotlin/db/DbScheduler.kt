package db

import model.Scheduler
import model.movie.Movie
import model.movie.Movies
import model.screening.Screenings
import repository.MovieRepository
import repository.ScreeningRepository
import java.time.LocalDate

class DbScheduler(
    private val movieRepository: MovieRepository,
    private val screeningRepository: ScreeningRepository,
) : Scheduler() {
    override fun getMovies(): Movies = movieRepository.findAll()

    override fun getScreenings(
        movie: Movie,
        date: LocalDate,
    ): Screenings {
        val movieId = movieRepository.findIdByTitle(movie.title) ?: return Screenings(emptyList())
        return screeningRepository.findByMovieIdAndDate(movieId, date)
    }
}
