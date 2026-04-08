package seat

import model.MovieReservationResult
import model.SeatGroup
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SeatGroupTest {
    @Test
    fun `예약되지 않은 좌석에 예약을 성공하면 예약 성공 결과를 반환한다`() {
        val seats =
            listOf(
                Seat(
                    row = SeatRow("A"),
                    column = SeatColumn(1),
                    grade = SeatGrade.A,
                    state = SeatState.AVAILABLE,
                ),
            )
        assertThat(
            SeatGroup(
                seats = seats,
            ).reserve(
                row = SeatRow("A"),
                column = SeatColumn(1),
            ),
        ).isEqualTo(MovieReservationResult.SUCCESS)
    }

    @Test
    fun `예약된 좌석에 예약을 시도하면 예약 실패 결과를 반환한다`() {
        val seats =
            listOf(
                Seat(
                    row = SeatRow("A"),
                    column = SeatColumn(1),
                    grade = SeatGrade.A,
                    state = SeatState.RESERVED,
                ),
            )
        assertThat(
            SeatGroup(
                seats = seats,
            ).reserve(
                row = SeatRow("A"),
                column = SeatColumn(1),
            ),
        ).isEqualTo(MovieReservationResult.FAILED)
    }

    @Test
    fun `구매완료된 좌석에 예약을 시도하면 예약 실패 결과를 반환한다`() {
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
            ).reserve(
                row = SeatRow("A"),
                column = SeatColumn(1),
            ),
        ).isEqualTo(MovieReservationResult.FAILED)
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
