package domain.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatsTest {
    @Test
    fun `좌석 목록 객체는 상영관 전체 좌석 구성을 갖는다`() {
        val seats = Seats.createDefault()

        assertThat(seats.size() == 20)
    }

    @Test
    fun `좌석 목록 객체는 특정 좌석이 존재하는지 판단할 수 있다`() {
        val seats = Seats.createDefault()

        seats.hasSeat(Seat("C", 1, SeatGrade.S))
    }
}
