package domain.screening

import domain.seat.Seat
import domain.seat.SeatGrade
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScreenTest {

    @Test
    fun `상영관은 상영관ID와 좌석 목록을 갖는다`(){

        val screen = Screen(1, listOf(
            Seat("A", 1, SeatGrade.B),
            Seat("A", 2, SeatGrade.B),
            Seat("A", 3, SeatGrade.B),
            Seat("A", 4, SeatGrade.B),
        ))

        assert(screen.id == 1)
        assert(screen.seats.size == 4)
    }

    @Test
    fun `상영관 번호는 0보다 커야 한다`(){
        val exception = assertThrows<IllegalArgumentException> {
            Screen(0, listOf())
        }
        assert(exception.message == "상영관 ID는 0보다 커야 합니다.")
    }
    @Test
    fun `상영관 좌석 목록은 비어있을 수 없다`(){
        val exception = assertThrows<IllegalArgumentException> {
            Screen(1, listOf())
        }
        assert(exception.message == "좌석 목록은 비어 있을 수 없습니다.")
    }
}
