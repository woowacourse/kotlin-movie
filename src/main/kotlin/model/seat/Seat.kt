package model.seat

data class Seat(
    val row: String,
    val column: Int,
    val isReserved: Boolean = false,
    val seatRank: SeatRank,
) {
    // 좌석의 이름을 불러오는 함수
    fun getSeatName(): String = "$row$column"

    // 좌석을 예약하는 함수
    fun reserve(): Seat {
        require(!isReserved) { "이미 예약된 좌석입니다" }
        return copy(
            isReserved = true,
        )
    }
}
