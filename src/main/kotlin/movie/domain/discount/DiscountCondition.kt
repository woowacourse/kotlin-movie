package movie.domain.discount

import java.time.LocalDate
import java.time.LocalTime

class DiscountCondition {
    fun isMovieDay(date: LocalDate): Boolean {
        return (date.dayOfMonth in setOf(10, 20, 30))
    }

    fun isTime(startTime: LocalTime): Boolean {
        return (startTime < LocalTime.of(11, 0, 0)
                || startTime > LocalTime.of(20, 0, 0))
    }
}
