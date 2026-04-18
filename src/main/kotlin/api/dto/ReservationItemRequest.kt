package api.dto

data class ReservationItemRequest(
    val screeningId: Int,
    val seats: List<String>,
)
