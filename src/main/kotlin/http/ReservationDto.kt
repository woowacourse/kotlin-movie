package http

data class ReservationDto(
    val screeningId: Int,
    val seats: List<String>,
)
