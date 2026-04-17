package movie.api.dto.reservation

class ReservationItemRequest(
    val screeningId: Long,
    val seats: List<String>,
)
