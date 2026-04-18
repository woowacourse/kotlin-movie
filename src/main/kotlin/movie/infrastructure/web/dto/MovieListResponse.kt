package movie.infrastructure.web.dto

import movie.domain.movie.Movie
import movie.domain.movie.Movies
import movie.domain.screening.Screening
import movie.domain.screening.Screenings

data class MovieListResponse(
    val movies: List<MovieResponse>,
) {
    companion object {
        fun from(movies: Movies): MovieListResponse = MovieListResponse(movies.toList().map(MovieResponse::from))
    }
}

data class MovieResponse(
    val movieId: Long,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
) {
    companion object {
        fun from(movie: Movie): MovieResponse =
            MovieResponse(
                movieId = movie.id,
                title = movie.title.toString(),
                runningTimeMinutes = movie.runningTimeMinutes,
                screenings = ScreeningResponse.from(movie.screenings),
            )
    }
}

data class ScreeningResponse(
    val screeningId: Long,
    val date: String,
    val startTime: String,
    val endTime: String,
) {
    companion object {
        fun from(screenings: Screenings): List<ScreeningResponse> = screenings.toList().map(::from)

        fun from(screening: Screening): ScreeningResponse =
            ScreeningResponse(
                screeningId = screening.id,
                date = screening.dateText(),
                startTime = screening.startTimeText(),
                endTime = screening.endTimeText(),
            )
    }
}
