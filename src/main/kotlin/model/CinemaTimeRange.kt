package model

data class CinemaTimeRange(
    val start: CinemaTime,
    val end: CinemaTime,
) {
    init {
        require(start.isBefore(end)) { "시작 시간이 종료 시간보다 늦을 수 없습니다" }
    }

    val durationMinute = start.minuteUntil(end)

    fun contains(time: CinemaTime): Boolean {
        if (time == start || time == end) return true
        return time.isAfter(start) && time.isBefore(end)
    }

    fun isEqual(cinemaTimeRange: CinemaTimeRange): Boolean = start == cinemaTimeRange.start && end == cinemaTimeRange.end

    fun overlaps(other: CinemaTimeRange): Boolean = !start.isAfter(other.end) && !other.start.isAfter(end)
}
