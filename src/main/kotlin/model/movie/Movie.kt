package model.movie

import model.CinemaTimeRange

class Movie(
    val name: MovieName,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(cinemaTimeRange: CinemaTimeRange): Boolean = runningTime.isSameDuration(cinemaTimeRange)

    fun isEqual(movieName: MovieName): Boolean = name == movieName

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.name == other.name && this.runningTime == other.runningTime
        }
        return false
    }

    override fun hashCode(): Int = name.hashCode()
}
