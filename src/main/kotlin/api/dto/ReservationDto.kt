package api.dto

data class ReservationRequest(
    val reservations: List<ReservationItemDto>,
    val usedPoints: Int,
    val paymentMethod: String
)

data class ReservationItemDto(
    val screeningId: Long,
    val seats: List<String>
)

data class ReservationResponse(
    val reservationId: Long,
    val reservations: List<ReservationItemDto>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int
)
