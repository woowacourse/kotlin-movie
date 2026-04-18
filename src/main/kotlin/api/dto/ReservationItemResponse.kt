package api.dto

data class ReservationItemResponse(
    val screeningId: Int,
    val seats: List<String>,
)
