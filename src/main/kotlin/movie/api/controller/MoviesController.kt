package movie.api.controller

import movie.api.dto.MovieResponse
import movie.api.dto.MoviesResponse
import movie.api.dto.ScreeningResponse
import movie.repository.inmemory.InMemoryScreenRepository
import movie.repository.jdbc.JdbcMovieRepository
import movie.repository.jdbc.JdbcScreeningRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MoviesController {
    val screenRepository = InMemoryScreenRepository
    val movieRepository = JdbcMovieRepository(isLocal = false)
    val screeningRepository =
        JdbcScreeningRepository(
            isLocal = false,
            screenRepository = screenRepository,
            movieRepository = movieRepository,
        )

    @GetMapping("/api/movies")
    fun getAllMovies(): MoviesResponse {
        val screenings = screeningRepository.getAllScreenings()
        val moviesResponse = mutableListOf<MovieResponse>()

        screenings.groupBy { it.movie }.forEach { (movie, screenings) ->
            moviesResponse.add(
                MovieResponse(
                    id = requireNotNull(movie.id) { "movie의 id 가 없습니다." },
                    title = movie.title,
                    runningTimeMinutes = movie.runningTime.minute,
                    screenings =
                        screenings.map {
                            ScreeningResponse(
                                id = requireNotNull(it.id) { "screening의 id 가 없습니다." },
                                startAt = it.startDateTime,
                                endAt = it.endDateTime,
                            )
                        },
                ),
            )
        }
        return MoviesResponse(moviesResponse)
    }
}
