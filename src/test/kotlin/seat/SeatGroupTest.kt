package seat

import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SeatGroupTest {
    @Test
    fun `존재하는 좌석을 요청하면 해당 좌석을 반환한다`() {
        val seats =
            listOf(
                Seat(
                    row = SeatRow("A"),
                    column = SeatColumn(1),
                    grade = SeatGrade.A,
                    state = SeatState.PURCHASED,
                ),
            )
        assertThat(
            SeatGroup(
                seats = seats,
            ).getSeat(
                row = SeatRow("A"),
                column = SeatColumn(1),
            ),
        ).isEqualTo(
            Seat(
                row = SeatRow("A"),
                column = SeatColumn(1),
                grade = SeatGrade.A,
                state = SeatState.PURCHASED,
            ),
        )
    }

    @Test
    fun `중복된 좌석이 존재하면 예외를 던진다`() {
        val seats =
            listOf(
                Seat(
                    row = SeatRow("A"),
                    column = SeatColumn(1),
                    grade = SeatGrade.A,
                    state = SeatState.PURCHASED,
                ),
                Seat(
                    row = SeatRow("A"),
                    column = SeatColumn(1),
                    grade = SeatGrade.A,
                    state = SeatState.PURCHASED,
                ),
            )
        assertThatThrownBy {
            SeatGroup(seats = seats)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
