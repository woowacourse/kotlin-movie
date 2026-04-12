package domain.common

import java.time.LocalTime

data class TimeRange(
    val start: LocalTime,
    val end: LocalTime,
) {
    init {
        require(end.isAfter(start)) { "종료시간이 시작시간보다 앞설 수 없습니다." }
    }

    fun isOverlapping(other: TimeRange): Boolean = this.start.isBefore(other.end) && this.end.isAfter(other.start)
}
