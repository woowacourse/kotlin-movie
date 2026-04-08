package domain.screening

import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.Seats
import java.util.UUID

class Screen(
    val id: Int,
    val seats: Seats
){
    init{
        require(id > 0) { "상영관 ID는 0보다 커야 합니다." }
    }
}

