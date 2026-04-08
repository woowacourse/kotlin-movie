package domain.screening

import domain.seat.Seat
import java.util.UUID

class Screen(
    val id: Int,
    val seats: List<Seat>,
){
    init{
        require(id > 0) { "상영관 ID는 0보다 커야 합니다." }
        require(seats.isNotEmpty()) { "좌석 목록은 비어 있을 수 없습니다." }
    }
}

