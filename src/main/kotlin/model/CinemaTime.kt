package model

import java.time.Duration
import java.time.LocalDateTime

@JvmInline
value class CinemaTime(
    val time: LocalDateTime,
) {
    fun isBefore(other: CinemaTime): Boolean = time.isBefore(other.time)

    fun isAfter(other: CinemaTime): Boolean = time.isAfter(other.time)

    fun isEqual(other: CinemaTime): Boolean = time.isEqual(other.time)

    fun isSameDay(dayOfMonth: Int): Boolean = time.dayOfMonth == dayOfMonth

    fun toLocalTime() = time.toLocalTime()

    fun minuteUntil(other: CinemaTime): Int = Duration.between(time, other.time).toMinutes().toInt()
}
