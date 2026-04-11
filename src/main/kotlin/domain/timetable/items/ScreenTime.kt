package domain.timetable.items

import java.time.LocalDate
import java.time.LocalTime

class ScreenTime(
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val screeningDate: LocalDate,
) {
    fun isContainsTime(time: LocalTime): Boolean = time in startTime..endTime

    fun isScreeningAt(date: LocalDate): Boolean = screeningDate == date

    fun isDuplicatedScreenTime(otherTime: ScreenTime): Boolean {
        if (this.screeningDate != otherTime.screeningDate) return true

        return this.startTime < otherTime.endTime && otherTime.startTime < this.endTime
    }
}
