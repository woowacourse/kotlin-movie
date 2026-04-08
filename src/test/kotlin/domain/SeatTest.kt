package domain

import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `동일한 SeatNumber와 SeatGrade를 가진 두 Seat는 동등하다`() {
        val seat1 = Seat(SeatNumber('A', 1), SeatGrade.A)
        val seat2 = Seat(SeatNumber('A', 1), SeatGrade.A)

        assertThat(seat1).isEqualTo(seat2)
    }

    @Test
    fun `SeatGrade가 다른 두 Seat는 동등하지 않다`() {
        val seat1 = Seat(SeatNumber('A', 1), SeatGrade.A)
        val seat2 = Seat(SeatNumber('A', 1), SeatGrade.S)

        assertThat(seat1).isNotEqualTo(seat2)
    }
}
