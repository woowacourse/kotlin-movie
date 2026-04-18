package api.dto.reservation

data class ReservationItemRequest(
    val screeningId: String,
    val seats: List<String>,
)
