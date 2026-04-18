package api.dto.movie

data class MovieResponse(
    val id: String,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
)
