package model.movie

import model.time.CinemaTimeRange

data class Movie(
    val name: MovieName,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(cinemaTimeRange: CinemaTimeRange): Boolean = runningTime.isSameDuration(cinemaTimeRange)

    fun isEqual(movieName: MovieName): Boolean = name == movieName
}
