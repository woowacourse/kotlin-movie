package model.schedule

import model.movie.MovieId

class CinemaSchedule(
    screenSchedules: List<ScreenSchedule>,
) {
    private val screenSchedules = screenSchedules.toList()

    init {
        require(screenSchedules.distinct().size == screenSchedules.size)
    }

    fun getMovieScreenings(movieId: MovieId): List<MovieScreening> = screenSchedules.flatMap { it.screeningOf(movieId) }
}
