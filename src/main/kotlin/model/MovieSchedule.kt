package model

class MovieSchedule(
    movieScreenings: List<MovieScreening>,
) {
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

    override fun hashCode(): Int = scheduledScreens.hashCode()
}
