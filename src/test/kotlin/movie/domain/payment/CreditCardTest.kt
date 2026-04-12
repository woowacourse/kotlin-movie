package movie.domain.payment

import movie.domain.amount.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreditCardTest {
    @Test
    fun `5% 할인을 적용한다`() {
        // given
        val creditCard = PaymentMethod.CreditCard()

        // when
        val result = creditCard.applyDiscount(Price(10000))

        // then
        assertThat(result).isEqualTo(Price(9500))
    }
}
