package movie.domain.amount

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MoneyTest {
    @Test
    fun `금액끼리 더할 수 있다`() {
        // given
        val price = Price(10000)

        // when
        val result = price.plus(Price(20000))

        // then
        assertThat(result).isEqualTo(Price(30000))
    }

    @Test
    fun `금액끼리 뺄 수 있다`() {
        // given
        val price = Price(20000)

        // when
        val result = price.minus(Price(10000))

        // then
        assertThat(result).isEqualTo(Price(10000))
    }

    @Test
    fun `비율 계산을 할 수 있다`() {
        // given
        val price = Price(10000)

        // when
        val result = price.percentOf(10)

        // then
        assertThat(result).isEqualTo(Price(1000))
    }

    @Test
    fun `금액이 0 미만이 되면 0원으로 보장된다`() {
        // given
        val price = Price(20000)

        // when
        val result = price.minus(Price(30000))

        // then
        assertThat(result).isEqualTo(Price(0))
    }
}
