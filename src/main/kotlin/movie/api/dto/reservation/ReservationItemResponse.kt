package movie.api.dto.reservation

data class ReservationItemResponse(
    val screeningId: Long,
    val seats: List<String>,
)
