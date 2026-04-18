package api.controller

import api.dto.MovieDto
import api.dto.MoviesResponse
import api.dto.ScreeningDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import repository.MovieRepository
import repository.ScreeningRepository

@RestController
@RequestMapping("/api/movies")
class MovieApiController(
    private val movieRepository: MovieRepository,
    private val screeningRepository: ScreeningRepository
) {

    @GetMapping
    fun getMovies(): MoviesResponse {
        val movies = movieRepository.findAll()
        val screenings = screeningRepository.findAll()
        
        val movieDtos = movies.map { movie ->
            val movieScreenings = screenings.filter { it.movie.id == movie.id }
                .map { screening ->
                    ScreeningDto(
                        id = screening.id!!,
                        startAt = screening.startTime,
                        endAt = screening.startTime.plusMinutes(movie.runningTime.duration.toLong())
                    )
                }
            
            MovieDto(
                id = movie.id!!,
                title = movie.title.title,
                runningTimeMinutes = movie.runningTime.duration,
                screenings = movieScreenings
            )
        }
        
        return MoviesResponse(movieDtos)
    }
}
