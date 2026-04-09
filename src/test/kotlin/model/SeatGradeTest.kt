package model

import model.seat.SeatGrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatGradeTest {
    @Test
    fun `S등급의 가격은 18,000원이다`() {
        // given & when
        val seatGrade = SeatGrade.S

        // then
        assertThat(seatGrade.price).isEqualTo(Money(18_000))
    }

    @Test
    fun `A등급의 가격은 15,000원이다`() {
        // given & when
        val seatGrade = SeatGrade.A

        // then
        assertThat(seatGrade.price).isEqualTo(Money(15_000))
    }

    @Test
    fun `B등급의 가격은 12,000원이다`() {
        // given & when
        val seatGrade = SeatGrade.B

        // then
        assertThat(seatGrade.price).isEqualTo(Money(12_000))
    }
}
