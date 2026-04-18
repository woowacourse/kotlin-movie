package movie.infrastructure.web.dto

data class ReservationResponse(
    val reservationId: Long,
    val reservations: List<ReservationItemResponse>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)

data class ReservationItemResponse(
    val screeningId: Long,
    val seats: List<String>,
)
