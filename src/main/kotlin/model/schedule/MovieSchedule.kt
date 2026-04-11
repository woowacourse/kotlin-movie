package model.schedule

import model.CinemaTime

class MovieSchedule(
    movieScreenings: List<MovieScreening>,
) : Iterable<MovieScreening> {
    private val movieScreenings = movieScreenings.toList()

    operator fun get(startTime: CinemaTime): MovieScreening = movieScreenings.first { it.screenTime.start.isEqual(startTime) }

    val size: Int = movieScreenings.size

    init {
        require(movieScreenings.distinctBy { it.movie }.size <= 1) {
            "일정에 포함된 영화들은 모두 동일한 영화여야 합니다."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is MovieSchedule) {
            return movieScreenings == other.movieScreenings
        }
        return false
    }

    fun toList(): List<MovieScreening> = movieScreenings

    fun getMovieSchedule(time: CinemaTime): MovieSchedule =
        MovieSchedule(
            movieScreenings.filter { screen ->
                screen.screenTime.start.isEqualDate(time)
            },
        )

    fun isEmpty(): Boolean = movieScreenings.isEmpty()

    override fun hashCode(): Int = movieScreenings.hashCode()

    override fun iterator(): Iterator<MovieScreening> = movieScreenings.iterator()
}
