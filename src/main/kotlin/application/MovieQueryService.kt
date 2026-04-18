package application

import api.dto.movie.MovieListResponse
import api.dto.movie.MovieResponse
import api.dto.movie.ScreeningResponse
import org.springframework.stereotype.Service
import persistence.CinemaDatabase
import persistence.ScreeningIdGenerator

@Service
class MovieQueryService(
    private val cinemaDatabase: CinemaDatabase,
) {
    fun getMovies(): MovieListResponse {
        val movieTheater = cinemaDatabase.loadMovieTheater()
        val screeningsByMovieId = movieTheater.screenings.groupBy { it.movie.id.value }

        val movies =
            movieTheater.movies.map { movie ->
                val screenings =
                    screeningsByMovieId[movie.id.value]
                        .orEmpty()
                        .sortedBy { it.startTime }
                        .map { screening ->
                            ScreeningResponse(
                                id = ScreeningIdGenerator.generate(screening),
                                startAt = screening.startTime.toApiDateTime(),
                                endAt = screening.endTime.toApiDateTime(),
                            )
                        }

                MovieResponse(
                    id = movie.id.value,
                    title = movie.title,
                    runningTimeMinutes = movie.runningTime,
                    screenings = screenings,
                )
            }

        return MovieListResponse(movies)
    }
}
