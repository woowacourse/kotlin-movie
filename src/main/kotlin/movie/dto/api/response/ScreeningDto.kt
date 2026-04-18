package movie.dto.api.response

import java.time.LocalDateTime

data class ScreeningDto(
    val id: Long,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)
