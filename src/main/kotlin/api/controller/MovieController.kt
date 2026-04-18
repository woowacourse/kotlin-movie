package api.controller

import api.dto.MoviesResponse
import api.service.MovieQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieQueryService: MovieQueryService,
) {
    @GetMapping
    fun getMovies(): MoviesResponse = movieQueryService.findAll()
}
