package http

data class ReservationsDto(
    val reservations: List<ReservationDto> = emptyList(),
    val usedPoints: Int,
    val paymentMethod: String,
)
