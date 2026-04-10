package model.movie

import model.CinemaTimeRange

class Movie(
    val name: MovieName,
    val id: MovieId,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(cinemaTimeRange: CinemaTimeRange): Boolean = runningTime.isSameDuration(cinemaTimeRange)

    fun isEqualId(movieId: MovieId): Boolean = id == movieId

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = name.hashCode()
}
