package domain.cinema

import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class MovieTime(val value: LocalDateTime) : Comparable<MovieTime> {
    val date: LocalDate get() = value.date
    val time: LocalTime get() = value.time
    val day: Int get() = value.day

    fun plusMinutes(minutes: Int): MovieTime {
        val moved = value
            .toInstant(TimeZone.currentSystemDefault())
            .plus(minutes.minutes)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return MovieTime(moved)
    }

    fun isOnSameDate(other: MovieTime): Boolean = date == other.date

    override fun compareTo(other: MovieTime): Int = value.compareTo(other.value)

    companion object {
        operator fun invoke(
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int,
        ): MovieTime = MovieTime(LocalDateTime(year, month, day, hour, minute))

        fun from(input: String): MovieTime {
            val date = runCatching { LocalDate.parse(input) }.getOrNull()
                ?: throw IllegalArgumentException("올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)")
            return MovieTime(LocalDateTime(date, LocalTime(0, 0)))
        }
    }
}
