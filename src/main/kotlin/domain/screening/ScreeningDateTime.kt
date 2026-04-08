package domain.screening

import java.time.LocalDate
import java.time.LocalTime

class ScreeningDateTime(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    fun isOverlapping(other: ScreeningDateTime): Boolean {
        return date == other.date && (startTime < other.endTime && endTime > other.startTime)
    }

    fun isMovieDay(): Boolean {
        return date.dayOfMonth == 10 || date.dayOfMonth == 20 || date.dayOfMonth == 30
    }

    fun isTimeDiscountTarget(): Boolean {
        return startTime.hour < 11 || endTime.hour > 20
    }
}
