package model.discount.payDiscountPolicy

import model.discount.PaymentMethod
import model.seat.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PaymentPayDiscountPolicyTest {
    @Test
    fun `결제 방식이 카드일 경우 5퍼센트 할인된 금액을 가진 Price가 반환된다`() {
        val paymentPayDiscountPolicy = PaymentPayDiscountPolicy(paymentMethod = PaymentMethod.CREDIT_CARD)
        val price = Price(45000)

        assertThat(paymentPayDiscountPolicy.calculatePrice(price)).isEqualTo(Price(42_750))
    }

    @Test
    fun `결제 방식이 현금일 경우 2퍼센트 할인된 금액을 가진 Price가 반환된다`() {
        val paymentPayDiscountPolicy = PaymentPayDiscountPolicy(paymentMethod = PaymentMethod.CASH)
        val price = Price(45000)

        assertThat(paymentPayDiscountPolicy.calculatePrice(price)).isEqualTo(Price(44_100))
    }
}
