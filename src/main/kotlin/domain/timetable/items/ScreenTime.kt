package domain.timetable.items

import java.time.LocalDate
import java.time.LocalTime

class ScreenTime(
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val screeningDate: LocalDate,
) {
    fun isContain(time: LocalTime): Boolean = startTime.isBefore(time) && endTime.isAfter(time)

    fun isStartAt(time: LocalTime): Boolean = startTime == time

    fun isScreeningAt(date: LocalDate): Boolean = screeningDate == date
}
