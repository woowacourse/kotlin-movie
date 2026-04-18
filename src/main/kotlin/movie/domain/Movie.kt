package movie.domain

import movie.error.MovieErrorMessage
import movie.domain.MovieTitle

class Movie(
    val title: MovieTitle,
    val runningTime: Int,
) {
    init {
        require(runningTime > 0) { MovieErrorMessage.INVALID_RUNNING_TIME }
    }
}
