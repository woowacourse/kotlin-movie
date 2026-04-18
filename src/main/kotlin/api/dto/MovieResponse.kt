package api.dto

data class MovieResponse(
    val id: Int,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
)
