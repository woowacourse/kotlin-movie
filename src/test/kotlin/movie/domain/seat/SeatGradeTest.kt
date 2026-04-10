package movie.domain.seat

import movie.domain.amount.Price
import org.junit.jupiter.api.Test

class SeatGradeTest {
    @Test
    fun `S 등급의 가격은 18000원이다`() {
        // given
        val seatGrade = SeatGrade.S

        // when
        val price = seatGrade.price

        // then
        assert(price == Price(18000))
    }

    @Test
    fun `A 등급의 가격은 15000원이다`() {
        // given
        val seatGrade = SeatGrade.A

        // when
        val price = seatGrade.price

        // then
        assert(price == Price(15000))
    }

    @Test
    fun `B 등급의 가격은 12000원이다`() {
        // given
        val seatGrade = SeatGrade.B

        // when
        val price = seatGrade.price

        // then
        assert(price == Price(12000))
    }
}
