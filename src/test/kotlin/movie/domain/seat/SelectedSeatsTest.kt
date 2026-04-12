package movie.domain.seat

import movie.domain.amount.Price
import org.junit.jupiter.api.Test

class SelectedSeatsTest {
    @Test
    fun `S석 2개, A석 1개 선택 시 총 가격은 51000원이다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                Seats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )

        // then
        assert(selectedSeats.totalPrice == Price(51000))
    }
}
