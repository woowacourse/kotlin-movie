package movie.domain.screening

import java.time.LocalDate
import java.time.LocalTime

class ScreeningDateTime(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    fun isOverlapping(other: ScreeningDateTime): Boolean = date == other.date && (startTime < other.endTime && endTime > other.startTime)

    fun isMovieDay(): Boolean = date.dayOfMonth == 10 || date.dayOfMonth == 20 || date.dayOfMonth == 30

    fun isTimeDiscountTarget(): Boolean = startTime.hour < 11 || endTime.hour > 20
}
