package model.seat

import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatRow
import model.seat.SeatState
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

    @Test
    fun `예약되지 않은 좌석에 예약을 성공하면 예약 성공 결과를 반환한다`() {
        val seat =
            Seat(
                row = SeatRow("A"),
                column = SeatColumn(1),
                grade = SeatGrade.A,
                state = SeatState.AVAILABLE,
            )

        assertThat(
            seat.reserve(),
        ).isTrue
    }

    @Test
    fun `예약된 좌석에 예약을 시도하면 예약 실패 결과를 반환한다`() {
        val seat =
            Seat(
                row = SeatRow("A"),
                column = SeatColumn(1),
                grade = SeatGrade.A,
                state = SeatState.RESERVED,
            )

        assertThat(
            seat.reserve(),
        ).isFalse
    }
}
