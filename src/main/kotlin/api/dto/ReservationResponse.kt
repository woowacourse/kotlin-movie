package api.dto

data class ReservationResponse(
    val reservationId: Int,
    val reservations: List<ReservationItemResponse>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
