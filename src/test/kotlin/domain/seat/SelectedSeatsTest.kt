package domain.seat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SelectedSeatsTest {
    @Test
    fun `S석 2개, A석 1개 선택 시 총 가격은 51000원이다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat("C", 1, SeatGrade.S),
                    Seat("C", 2, SeatGrade.S),
                    Seat("E", 1, SeatGrade.A),
                ),
            )

        // then
        assert(selectedSeats.totalPrice == 51000)
    }

    @Test
    fun `좌석 목록은 비어 있을 수 없다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                SelectedSeats(listOf())
            }
        assert(exception.message == "좌석 목록은 비어 있을 수 없습니다.")
    }

    @Test
    fun `중복된 좌석을 선택할 수 없다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                SelectedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 1, SeatGrade.S),
                    ),
                )
            }

        assert(exception.message == "중복된 좌석을 선택할 수 없습니다.")
    }
}
