package api.controller

import api.dto.MovieListResponse
import api.service.MovieService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MovieApiController(
    private val movieService: MovieService,
) {
    @GetMapping("/movies")
    fun getMovies(): MovieListResponse = movieService.findAll()
}
