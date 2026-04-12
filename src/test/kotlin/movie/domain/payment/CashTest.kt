package movie.domain.payment

import movie.domain.amount.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CashTest {
    @Test
    fun `2% 할인을 적용한다`() {
        // given
        val cash = PaymentMethod.Cash()

        // when
        val result = cash.applyDiscount(Price(10000))

        // then
        assertThat(result).isEqualTo(Price(9800))
    }
}
