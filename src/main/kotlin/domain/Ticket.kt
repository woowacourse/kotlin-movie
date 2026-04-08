package domain

data class Ticket(
    val screening: Screening,
    val seatPositions: List<SeatPosition>
) {
    init {
        require(seatPositions.distinct().size == seatPositions.size) { "중복된 좌석이 존재합니다" }
    }
}
