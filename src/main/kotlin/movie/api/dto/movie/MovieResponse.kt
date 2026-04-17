package movie.api.dto.movie

import movie.api.dto.screening.ScreeningResponse
import movie.domain.movie.Movie

data class MovieResponse(
    val id: Long,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
) {
    companion object {
        fun from(
            movie: Movie,
            runningTimeMinutes: Int,
        ): MovieResponse =
            MovieResponse(
                id = movie.id,
                title = movie.title,
                runningTimeMinutes = runningTimeMinutes,
                screenings =
                    movie.screenings.screenings.map { screening ->
                        ScreeningResponse.from(screening)
                    },
            )
    }
}
