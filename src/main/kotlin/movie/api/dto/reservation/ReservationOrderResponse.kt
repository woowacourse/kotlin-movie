package movie.api.dto.reservation

data class ReservationOrderResponse(
    val reservationId: Long,
    val reservations: List<ReservationItemResponse>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
