package model

import java.time.Duration
import java.time.LocalDateTime

data class DateTimeRange(
    val start: LocalDateTime,
    val end: LocalDateTime,
) {
    init {
        require(start.isBefore(end)) { "시작 시간이 종료 시간보다 늦을 수 없습니다" }
    }

    val durationMinute = Duration.between(start, end).toMinutes().toInt()

    fun contains(time: LocalDateTime): Boolean {
        if (time.isEqual(start) || time.isEqual(end)) return true
        return time.isAfter(start) && time.isBefore(end)
    }

    fun overlaps(other: DateTimeRange): Boolean = !start.isAfter(other.end) && !other.start.isAfter(end)
}
