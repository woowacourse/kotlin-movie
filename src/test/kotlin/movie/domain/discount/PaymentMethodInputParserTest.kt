package movie.domain.discount

import movie.domain.payment.PaymentMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentMethodInputParserTest {
    @Test
    fun `1이 입력되면 CreditCard가 반환된다`() {
        // given
        val input = 1

        // when
        val paymentMethod = PaymentMethodInputParser.parse(input)

        // then
        assertThat(paymentMethod).isEqualTo(PaymentMethod.CreditCard)
    }

    @Test
    fun `2가 입력되면 Cash가 반환된다`() {
        // given
        val input = 2

        // when
        val paymentMethod = PaymentMethodInputParser.parse(input)

        // then
        assertThat(paymentMethod).isEqualTo(PaymentMethod.Cash)
    }

    @Test
    fun `1 또는 2가 아닌 값이 입력되면 예외가 발생한다`() {
        // given
        val input = 3

        // when
        val exception =
            assertThrows<IllegalArgumentException> {
                PaymentMethodInputParser.parse(input)
            }

        // then
        assertThat(exception.message).isEqualTo("유효하지 않은 결제 수단입니다.")
    }
}
