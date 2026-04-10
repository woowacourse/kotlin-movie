package model.schedule

import model.CinemaTime
import model.CinemaTimeRange
import model.movie.MovieId
import model.movie.MovieName

class ScreenSchedule(
    private val screenId: String,
    private val servicePeriod: CinemaTimeRange,
    movieScreenings: List<MovieScreening>,
) {
    private val movieScreenings = movieScreenings.toList()

    init {
        require(movieScreenings.all { isContainServicePeriod(it.screenTime.start) }) { "상영관의 운영 시작 시간보다 일찍 영화를 배정할 수 없습니다." }
        require(movieScreenings.all { isContainServicePeriod(it.screenTime.end) }) { "상영관의 운영 종료 시간보다 늦게 영화를 배정할 수 없습니다." }
        movieScreenings.forEachIndexed { index, current ->
            movieScreenings.drop(index + 1).forEach { other ->
                require(!current.screenTime.overlaps(other.screenTime)) {
                    "상영 시간이 겹칩니다: ${current.screenTime} / ${other.screenTime}"
                }
            }
        }
    }

    fun isContainServicePeriod(time: CinemaTime): Boolean = servicePeriod.contains(time)

    fun getMovieSchedule(movieId: MovieId): MovieSchedule =
        MovieSchedule(movieScreenings.filter { it.movie.isEqualId(movieId) })

    override fun equals(other: Any?): Boolean {
        if (other is ScreenSchedule) {
            return this.screenId == other.screenId
        }
        return false
    }

    override fun hashCode(): Int = screenId.hashCode()
}
