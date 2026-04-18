package http

data class ReservationResponse(
    val reservationId: Int,
    val reservations: List<ReservationDto>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
