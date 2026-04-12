package movie.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PriceTest {
    @Test
    fun `0원 이상으로 가격을 생성 시 올바른 객체가 생성된다`() {
        val price1 = Price(10_000)
        val price2 = Price(0)

        assertThat(price1.amount).isEqualTo(10_000)
        assertThat(price2.amount).isEqualTo(0)
    }

    @Test
    fun `가격이 0원 미만일 경우 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            Price(-1000)
        }
    }

    @Test
    fun `가격을 더하면 금액이 더해진 가격을 반환한다`() {
        val price1 = Price(10_000)
        val price2 = Price(5_000)

        assertThat(price1 + price2).isEqualTo(Price(15_000))
    }

    @Test
    fun `할인율을 받아 할인된 가격을 반환한다`() {
        val price = Price(10_000)

        assertThat(price.getDiscountPrice(0.1f)).isEqualTo(Price(9000))
    }
}
