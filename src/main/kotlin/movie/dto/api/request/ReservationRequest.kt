package movie.dto.api.request

data class ReservationRequest(
    val reservations: List<ReservationDto>,
    val usedPoints: Int,
    val paymentMethod: String
)
