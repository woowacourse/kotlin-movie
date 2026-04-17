package movie.domain.discount

import movie.domain.amount.Price
import movie.domain.payment.PaymentMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PaymentMethodDiscountPolicyTest {
    @Test
    fun `현금은 2% 할인을 적용한다`() {
        // given
        val cash = PaymentMethod.Cash
        val paymentDiscountPolicy = PaymentMethodDiscountPolicy()

        // when
        val result = paymentDiscountPolicy.applyDiscount(Price(10000), cash)

        // then
        assertThat(result).isEqualTo(Price(9800))
    }

    @Test
    fun `신용카드는 5% 할인을 적용한다`() {
        // given
        val creditCard = PaymentMethod.CreditCard
        val paymentDiscountPolicy = PaymentMethodDiscountPolicy()

        // when
        val result = paymentDiscountPolicy.applyDiscount(Price(10000), creditCard)

        // then
        assertThat(result).isEqualTo(Price(9500))
    }
}
