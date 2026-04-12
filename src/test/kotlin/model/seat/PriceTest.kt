package model.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PriceTest {
    @Test
    fun `금액은 0 미만이면 오류가 발생한다`() {
        val value = -1000

        assertThrows<IllegalArgumentException> {
            Price(value)
        }
    }

    @Test
    fun `금액이 0 이상이어야 한다`() {
        val value = 2000
        val price = Price(value)

        assertThat(price.value).isEqualTo(2000)
    }
}
