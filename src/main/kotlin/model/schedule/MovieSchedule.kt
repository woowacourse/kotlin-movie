package model.schedule

import model.CinemaTime

class MovieSchedule(
    movieScreenings: List<MovieScreening>,
) : Iterable<MovieScreening> {
    init {
        require(movieScreenings.distinctBy { it.movie }.size <= 1) {
            "일정에 포함된 영화들은 모두 동일한 영화여야 합니다."
        }
    }

    private val scheduledScreens = movieScreenings.toList()
    val size: Int = scheduledScreens.size

    operator fun get(index: Int): MovieScreening = scheduledScreens[index]

    fun getMovieSchedule(time: CinemaTime): MovieSchedule =
        MovieSchedule(
            scheduledScreens.filter { screen ->
                screen.screenTime.start.isEqualDate(time)
            },
        )

    fun getMovieScreening(time: CinemaTime): MovieScreening = scheduledScreens.first { it.screenTime.start == time }

    fun isEmpty(): Boolean = scheduledScreens.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (other is MovieSchedule) {
            return scheduledScreens == other.scheduledScreens
        }
        return false
    }

    override fun hashCode(): Int = scheduledScreens.hashCode()

    override fun iterator(): Iterator<MovieScreening> = scheduledScreens.iterator()
}
