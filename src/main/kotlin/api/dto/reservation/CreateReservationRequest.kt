package api.dto.reservation

data class CreateReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)
