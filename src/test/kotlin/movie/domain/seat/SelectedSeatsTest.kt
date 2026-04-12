package movie.domain.seat

import movie.domain.amount.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SelectedSeatsTest {
    @Test
    fun `S석 2개, A석 1개 선택 시 총 가격은 51000원이다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                    Seat(SeatRow("C"), SeatColumn(2), SeatGrade.S),
                    Seat(SeatRow("E"), SeatColumn(1), SeatGrade.A),
                ),
            )

        // then
        assert(selectedSeats.totalPrice == Money(51000))
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
                        Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                        Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                    ),
                )
            }

        assert(exception.message == "중복된 좌석을 선택할 수 없습니다.")
    }

    @Test
    fun `좌석 위치 목록과 상영관 좌석 목록으로 선택 좌석을 생성할 수 있다`() {
        val positions =
            SeatPositions(
                listOf(
                    SeatPosition(SeatRow("A"), SeatColumn(1)),
                    SeatPosition(SeatRow("C"), SeatColumn(2)),
                ),
            )

        val selectedSeats = SelectedSeats.from(positions, Seats.createDefault())

        assertThat(selectedSeats.display()).isEqualTo("A1, C2")
    }
}
