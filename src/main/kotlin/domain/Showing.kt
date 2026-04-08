package domain

import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class Showing(val startTime: LocalDateTime, val screen: Screen, val movie: Movie) {
    val endTime = startTime
        .toInstant(TimeZone.currentSystemDefault())
        .plus(movie.runningTime.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
