package movie.api.controller.movie

import movie.api.dto.movie.MoviesResponse
import movie.data.db.DatabaseManager
import movie.data.db.movie.MovieRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MovieApiController {
    @GetMapping("/movies")
    fun getMovies(): ResponseEntity<MoviesResponse> =
        DatabaseManager.connection.use { connection ->
            val movieRepository = MovieRepository(connection)
            val movies = movieRepository.findAll()
            val runningTimeMap = movieRepository.findRunningTimeMap()

            ResponseEntity.ok(MoviesResponse.Companion.from(movies, runningTimeMap))
        }
}
