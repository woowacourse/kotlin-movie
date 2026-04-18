package model.schedule

import model.CinemaTimeRange
import model.movie.Movie

class ScreenSchedule(
    private val screenId: String,
    private val servicePeriod: CinemaTimeRange,
    private val movieScreenings: List<MovieScreening>,
) {
    init {
        val outOfRange = movieScreenings.firstOrNull { !it.isWithin(servicePeriod) }
        require(outOfRange == null) {
            "상영관 $screenId 에서 운영 시간($servicePeriod)을 벗어난 영화가 있습니다. - $outOfRange"
        }
        movieScreenings.forEachIndexed { index, current ->
            movieScreenings.drop(index + 1).forEach { other ->
                require(!current.overlaps(other)) {
                    "상영관 $screenId 의 상영 시간이 겹칩니다: 영화 - $current / $other"
                }
            }
        }
    }

    fun screeningOf(movie: Movie): List<MovieScreening> =
        movieScreenings.filter {
            it.isSameMovie(movie)
        }

    override fun equals(other: Any?): Boolean {
        if (other is ScreenSchedule) {
            return this.screenId == other.screenId
        }
        return false
    }

    override fun hashCode(): Int = screenId.hashCode()
}
