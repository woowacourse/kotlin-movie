package movie.api.dto.screening

import movie.domain.screening.Screening
import java.time.LocalDateTime

data class ScreeningResponse(
    val id: Long,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    companion object {
        fun from(screening: Screening): ScreeningResponse =
            ScreeningResponse(
                id = screening.id,
                startAt = screening.screeningDateTime.startAt,
                endAt = screening.screeningDateTime.endAt,
            )
    }
}
