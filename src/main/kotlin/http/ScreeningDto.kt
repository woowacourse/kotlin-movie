package http

import java.time.LocalDateTime

data class ScreeningDto(
    val id: Long,
    val startTime: LocalDateTime,
    val endAt: LocalDateTime,
)
