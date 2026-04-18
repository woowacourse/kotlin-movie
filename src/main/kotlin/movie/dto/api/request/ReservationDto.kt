package movie.dto.api.request

data class ReservationDto(
    val screeningId: Long,
    val seats: List<String>
)