package api.dto.reservation

data class ReservationItemResponse(
    val screeningId: String,
    val seats: List<String>,
)
