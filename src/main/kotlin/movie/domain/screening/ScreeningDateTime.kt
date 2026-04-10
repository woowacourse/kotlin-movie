package movie.domain.screening

import java.time.LocalDate
import java.time.LocalTime

class ScreeningDateTime(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    init {
        require(startTime < endTime) { "시작 시간은 종료 시간보다 이전이어야 합니다." }
    }

    fun isOverlapping(other: ScreeningDateTime): Boolean = date == other.date && (startTime < other.endTime && endTime > other.startTime)
}
