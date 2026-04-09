package model.movie

import model.CinemaTimeRange

class Movie(
    private val id: String,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(cinemaTimeRange: CinemaTimeRange): Boolean = runningTime.isSameDuration(cinemaTimeRange)

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = id.hashCode()
}
