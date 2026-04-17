package movie.api.dto.reservation

data class ReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)
