package movie.domain.payment

import movie.domain.amount.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreditCardTest {
    @Test
    fun `5% 할인을 적용한다`() {
        // given
        val creditCard = CreditCard()

        // when
        val result = creditCard.applyDiscount(Money(10000))

        // then
        assertThat(result).isEqualTo(Money(9500))
    }
}
