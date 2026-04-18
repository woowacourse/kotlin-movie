package movie.infrastructure.web

import movie.infrastructure.web.dto.MovieListResponse
import movie.repository.MovieRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieApiController(
    private val movieRepository: MovieRepository,
) {
    @GetMapping
    fun getMovies(): MovieListResponse = MovieListResponse.from(movieRepository.findAll())
}
