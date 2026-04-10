package movie.domain.amount

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun `포인트가 잔액보다 큰 경우 잔액만큼을 사용할 수 있다`() {
        // given
        val point = Point(5000)
        val price = Price(3000)

        // when
        val usagePoint = point.usableAmount(price)

        // then
        assertThat(usagePoint).isEqualTo(Point(3000))
    }

    @Test
    fun `포인트가 잔액보다 작은 경우 보유한 포인트만큼을 사용할 수 있다`() {
        // given
        val point = Point(5000)
        val price = Price(10000)

        // when
        val usagePoint = point.usableAmount(price)

        // then
        assertThat(usagePoint).isEqualTo(Point(5000))
    }
}
