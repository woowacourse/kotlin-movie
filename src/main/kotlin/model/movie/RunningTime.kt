package model.movie

import model.DateTimeRange

@JvmInline
value class RunningTime(
    val minute: Int,
) {
    init {
        require(minute > 0)
    }

    fun isSameDuration(dateTimeRange: DateTimeRange): Boolean = dateTimeRange.durationMinute.toInt() == minute
}
