package api.dto

data class ReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)
