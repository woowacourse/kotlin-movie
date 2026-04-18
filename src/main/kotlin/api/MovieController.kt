package api

import api.dto.movie.MovieListResponse
import application.MovieQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieQueryService: MovieQueryService,
) {
    @GetMapping
    fun getMovies(): MovieListResponse = movieQueryService.getMovies()
}
