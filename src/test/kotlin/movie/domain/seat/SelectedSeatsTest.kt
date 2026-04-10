package movie.domain.seat

import movie.domain.amount.Price
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SelectedSeatsTest {
    @Test
    fun `S석 2개, A석 1개 선택 시 총 가격은 51000원이다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("C", 1, SeatGrade.S),
                    Seat("C", 2, SeatGrade.S),
                    Seat("E", 1, SeatGrade.A),
                ),
            )

        // then
        assert(selectedSeats.totalPrice == Price(51000))
    }

    @Test
    fun `좌석 목록은 비어 있을 수 없다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                SelectedSeats(setOf())
            }
        assert(exception.message == "좌석 목록은 비어 있을 수 없습니다.")
    }
}
