package movie.domain.seat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatTest {
    @Test
    fun `A1 좌석은 Row A, Column 1, B등급이다`() {
        // given
        val seat = Seat("A", 1, SeatGrade.B)

        // then
        assert(seat.row == "A")
        assert(seat.column == 1)
        assert(seat.grade == SeatGrade.B)
    }

    @Test
    fun `좌석행은 비어 있을 수 없다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Seat("", 1, SeatGrade.B)
            }
        assert(exception.message == "좌석 행은 비어 있을 수 없습니다.")
    }

    @Test
    fun `좌석행은 A에서 E 사이여야 한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Seat("F", 1, SeatGrade.B)
            }
        assert(exception.message == "좌석 행은 A~E 사이여야 합니다.")
    }

    @Test
    fun `좌석열은 1에서 4 사이어야 한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Seat("A", 5, SeatGrade.B)
            }
        assert(exception.message == "좌석 열은 1~4 사이여야 합니다.")
    }
}
