package movie.domain

import java.time.LocalDateTime

class Schedule(
    val movie: Movie,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
) {
    init {
        require(startTime < endTime) { "영화 시작 시각은 종료 시각보다 빨라야 합니다" }
    }
}
