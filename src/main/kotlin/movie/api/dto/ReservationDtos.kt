package movie.api.dto

data class ReservationRequest(
    val reservations: List<ScreeningReservationRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)

data class ScreeningReservationRequest(
    val screeningId: Long,
    val seats: List<String>,
)

data class ReservationResponse(
    val reservationId: Long,
    val reservations: List<ScreeningReservationRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
