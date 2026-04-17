package api.service

import api.dto.MovieListResponse
import api.dto.MovieResponse
import api.dto.ScreeningResponse
import org.springframework.stereotype.Service
import repository.MovieRepository
import repository.ScreeningRepository

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val screeningRepository: ScreeningRepository,
) {
    fun findAll(): MovieListResponse {
        val movies =
            movieRepository.findAll().map {
                (
                    movieId,
                    movie,
                ),
                ->
                val screenings =
                    screeningRepository
                        .findByMovieId(
                            movieId,
                            movie,
                        ).map { (screeningId, screening) ->
                            ScreeningResponse(
                                id = screeningId,
                                startAt =
                                    screening.startDateTime,
                                endAt =
                                    screening.endDateTime,
                            )
                        }
                MovieResponse(
                    id = movieId,
                    title = movie.title,
                    runningTimeMinutes =
                        movie.runningTime,
                    screenings = screenings,
                )
            }
        return MovieListResponse(movies)
    }
}
