package movie.domain.screening

import java.time.LocalDateTime

class ScreeningDateTime(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    init {
        require(startAt < endAt) { "시작 시간은 종료 시간보다 이전이어야 합니다." }
    }

    fun isOverlapping(other: ScreeningDateTime): Boolean = startAt < other.endAt && endAt > other.startAt
}
