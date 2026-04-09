package domain.timetable.items

import java.time.LocalDate
import java.time.LocalTime

class ScreenTime(
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val screeningDate: LocalDate,
) {
    fun isContain(time: LocalTime): Boolean = time in startTime..endTime

    fun isStartAt(time: LocalTime): Boolean = startTime == time

    fun isStartBefore(time: LocalTime): Boolean = startTime <= time

    fun isStartAfter(time: LocalTime): Boolean = startTime >= time

    fun isScreeningAt(date: LocalDate): Boolean = screeningDate == date

    fun screeningDayOfMonth(): Int = screeningDate.dayOfMonth

    fun startTimeToString() = startTime.toString()

    fun screeningDateToString() = screeningDate.toString()
}
