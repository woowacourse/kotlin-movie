package model.movie

import model.DateTimeRange

class Movie(
    private val id: String,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(dateTimeRange: DateTimeRange): Boolean = runningTime.isSameDuration(dateTimeRange)

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = id.hashCode()
}
