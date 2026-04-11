package domain.model.seat

// 좌석과 예약 가능 상태를 함께 표현한다.
data class SeatAvailability(
    val seat: Seat,
    val status: SeatStatus = SeatStatus.AVAILABLE,
) {
    // 요청 좌석과 동일한 좌석인지 확인한다.
    fun isSeat(targetSeat: Seat): Boolean = seat == targetSeat

    // 좌석이 현재 예약 가능한 상태인지 확인한다.
    fun isAvailable(): Boolean = status == SeatStatus.AVAILABLE

    // 좌석 상태를 예약됨으로 변경한 새 객체를 반환한다.
    fun reserve(): SeatAvailability = copy(status = SeatStatus.RESERVED)
}

// 좌석 예약 상태
enum class SeatStatus {
    AVAILABLE,
    RESERVED,
}
