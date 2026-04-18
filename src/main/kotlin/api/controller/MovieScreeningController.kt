package api.controller

import api.dto.MoviesResponse
import database.repository.MovieScreeningRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieScreeningController {
    private val movieScreeningRepository = MovieScreeningRepository()

    @GetMapping("/api/movies")
    fun getScreenings(): ResponseEntity<MoviesResponse> {
        val movies = movieScreeningRepository.findAllWithScreenings()
        return ResponseEntity.ok(MoviesResponse(movies = movies))
    }
}
