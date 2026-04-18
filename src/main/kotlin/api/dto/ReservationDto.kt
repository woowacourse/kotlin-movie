package api.dto

data class CreateReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int = 0,
    val paymentMethod: String,
)

data class ReservationItemRequest(
    val screeningId: Long,
    val seats: List<String>,
)

data class CreateReservationResponse(
    val reservationId: Long,
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
