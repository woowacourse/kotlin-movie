package movie.dto

import java.time.LocalDateTime

data class MovieScheduleDto(
    val movieId: Long,
    val scheduleId: Long,
    val title: String,
    val runningTime: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val reservedSeats: List<ReservedSeatDto> = emptyList()
)
