package movie.domain

import java.time.LocalDateTime

class Schedule(
    val movie: Movie,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
) {
    private val theater = Theater()

    init {
        require(startTime < endTime) { "영화 시작 시각은 종료 시각보다 빨라야 합니다" }
    }

    fun isDuplicateTime(target: Schedule): Boolean {
        return target.endTime in startTime..endTime
                || target.startTime in startTime..endTime
    }
}
