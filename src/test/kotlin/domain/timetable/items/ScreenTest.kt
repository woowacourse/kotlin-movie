package domain.timetable.items

import domain.seat.Seat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScreenTest {
    val seats =
        listOf(
            Seat("A1"),
            Seat("A2"),
            Seat("A3"),
            Seat("A4"),
            Seat("A5"),
        )

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하는 좌석이면 true가 반환된다`() {
        val screen =
            Screen(
                seats = seats,
            )

        val result = screen.findSeat("A1")
        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하지 않는 좌석이면 false가 반환된다`() {
        val screen =
            Screen(
                seats = seats,
            )

        val result = screen.findSeat("F1")
        assertThat(result).isFalse()
    }
}
