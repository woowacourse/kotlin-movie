package domain

import domain.seat.Column
import domain.seat.ReserveState
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `행과 열을 가진다`() {
        Seat(position = SeatPosition(row = Row.A, column = Column(1)))
    }

    @Test
    fun `열이 1~12 범위가 아닐 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Seat(position = SeatPosition(row = Row.A, column = Column(13)))
        }
    }

    @Test
    fun `좌석 위치에 따른 등급을 안다`() {
        val given = Seat(position = SeatPosition(row = Row.A, column = Column(1)))
        val expected = SeatGrade.B
        assertThat(given.grade).isEqualTo(expected)
    }

    @Test
    fun `좌석의 초기 예약 상태는 예약 가능한 상태이다`() {
        val given = Seat(position = SeatPosition(row = Row.A, column = Column(1)))
        val expected = ReserveState.AVAILABLE
        assertThat(given.state).isEqualTo(expected)
    }
}
