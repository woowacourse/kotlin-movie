import model.Seat
import model.SeatColumn
import model.SeatGrade
import model.SeatRow
import model.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `좌석 번호가 다르다면 다른 좌석으로 판단한다`() {
        assertThat(
            Seat(
                row = SeatRow(row = "A"),
                column = SeatColumn(column = 3),
                grade = SeatGrade.A,
                state = SeatState.RESERVED,
            ),
        ).isNotEqualTo(
            Seat(
                row = SeatRow(row = "B"),
                column = SeatColumn(column = 4),
                grade = SeatGrade.A,
                state = SeatState.RESERVED,
            ),
        )
    }

    @Test
    fun `좌석 번호가 같다면 같은 좌석으로 판단한다`() {
        assertThat(
            Seat(
                row = SeatRow(row = "A"),
                column = SeatColumn(column = 3),
                grade = SeatGrade.A,
                state = SeatState.RESERVED,
            ),
        ).isEqualTo(
            Seat(
                row = SeatRow(row = "A"),
                column = SeatColumn(column = 3),
                grade = SeatGrade.S,
                state = SeatState.AVAILABLE,
            ),
        )
    }
}
