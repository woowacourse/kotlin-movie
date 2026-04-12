package movie.domain.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatsTest {

    @Test
    fun `기본 좌석 목록은 20개의 좌석을 가진다`() {
        val seats = Seats.createDefault()

        assertThat(seats.size()).isEqualTo(20)
    }

    @Test
    fun `존재하는 좌석이면 true를 반환한다`() {
        val seats = Seats.createDefault()
        val target = Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S)

        assertThat(seats.hasSeat(target)).isTrue()
    }
}
