package model.schedule

import model.schedule.MovieScreening
import java.time.LocalDateTime

class MovieSchedule(
    movieScreenings: List<MovieScreening>,
) : Iterable<MovieScreening> {
    private val scheduledScreens = movieScreenings.toList()

    init {
        require(movieScreenings.distinctBy { it.movie }.size <= 1) {
            "일정에 포함된 영화들은 모두 동일한 영화여야 합니다."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is MovieSchedule) {
            return scheduledScreens == other.scheduledScreens
        }
        return false
    }

    fun getMovieScreening(time: LocalDateTime): MovieScreening = scheduledScreens.first { it.screenTime.start.isEqual(time) }

    override fun hashCode(): Int = scheduledScreens.hashCode()

    override fun iterator(): Iterator<MovieScreening> = scheduledScreens.iterator()
}
