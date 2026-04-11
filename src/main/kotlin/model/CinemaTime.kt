package model

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@JvmInline
value class CinemaTime(
    private val time: LocalDateTime,
) {
    fun isBefore(other: CinemaTime): Boolean = time.isBefore(other.time)

    fun isAfter(other: CinemaTime): Boolean = time.isAfter(other.time)

    fun isTimeOfDayAtOrBefore(other: LocalTime): Boolean = time.toLocalTime().isAfter(other).not()

    fun isTimeOfDayAtOrAfter(other: LocalTime): Boolean = time.toLocalTime().isBefore(other).not()

    fun isSameDayOfMonth(dayOfMonth: Int): Boolean = time.dayOfMonth == dayOfMonth

    fun isEqualDate(date: CinemaTime): Boolean = time.toLocalDate().isEqual(date.time.toLocalDate())

    fun format(format: String): String = time.format(DateTimeFormatter.ofPattern(format))

    override fun toString(): String = time.format(DateTimeFormatter.ofPattern("HH:mm"))

    fun minuteUntil(other: CinemaTime): Int = Duration.between(time, other.time).toMinutes().toInt()
}
