package api.dto.reservation

data class CreateReservationResponse(
    val reservationId: String,
    val reservations: List<ReservationItemResponse>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
