package domain.amount

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `잔액보다 적은 금액을 차감할 수 있다`() {
        // given
        val point = Point(5000)
        val money = Money(3000)

        // when
        val result = point.minus(money)

        // then
        assertThat(result).isEqualTo(Point(2000))
    }

    @Test
    fun `잔액보다 큰 금액을 차감하면 잔액만큼만 차감된다`() {
        // given
        val point = Point(5000)
        val money = Money(10000)

        // when
        val result = point.minus(money)

        // then
        assertThat(result).isEqualTo(Point(0))
    }
}
