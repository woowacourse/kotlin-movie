package model.schedule

import model.CinemaTime

class MovieSchedule(
    movieScreenings: List<MovieScreening>,
) : Iterable<MovieScreening> {
    private val scheduledScreens = movieScreenings.toList()

    operator fun get(index: Int): MovieScreening = scheduledScreens[index]

    val size: Int = scheduledScreens.size

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

    fun getMovieSchedule(time: CinemaTime): MovieSchedule =
        MovieSchedule(
            scheduledScreens.filter { screen ->
                screen.screenTime.start.isEqualDate(time)
            },
        )

    fun getMovieScreening(time: CinemaTime): MovieScreening = scheduledScreens.first { it.screenTime.start.isEqual(time) }

    fun isEmpty(): Boolean = scheduledScreens.isEmpty()

    override fun hashCode(): Int = scheduledScreens.hashCode()

    override fun iterator(): Iterator<MovieScreening> = scheduledScreens.iterator()
}
