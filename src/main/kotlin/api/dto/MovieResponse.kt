package api.dto

import java.time.LocalDateTime

data class MoviesResponse(
    val movies: List<MovieDto>
)

data class MovieDto(
    val id: Long,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningDto>
)

data class ScreeningDto(
    val id: Long,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)
