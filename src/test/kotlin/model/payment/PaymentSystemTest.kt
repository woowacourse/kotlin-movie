@file:Suppress("NonAsciiCharacters")

package model.payment

import model.Money
import model.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PaymentSystemTest {
    @Test
    fun `포인트가 예매 금액에서 올바르게 차감된다`() {
        // 할인 적용된 금액 18,000 → 포인트 2,000 차감 → 16,000 → 현금 2% → 15,680
        val paymentSystem = PaymentSystem(paymentMethod = Cash)

        val result = paymentSystem.pay(Money(18000), Point(2_000))

        assertThat(result.finalPrice).isEqualTo(Money(15_680))
    }

    @Test
    fun `포인트 차감 후 결제 수단 할인이 적용된다`() {
        // 할인 적용된 금액 18,000 → 포인트 2,000 차감 → 16,000 → 카드 5% → 15,200
        val paymentSystem = PaymentSystem(paymentMethod = Card)

        val result = paymentSystem.pay(Money(18000), Point(2_000))

        assertThat(result.finalPrice).isEqualTo(Money(15_200))
    }

    @Test
    fun `신용카드 결제 시 5퍼센트 추가 할인이 적용된다`() {
        // 할인 적용된 금액 18,000 → 카드 5% → 17,100
        val paymentSystem = PaymentSystem(paymentMethod = Card)

        val result = paymentSystem.pay(Money(18000), Point(0))

        assertThat(result.finalPrice).isEqualTo(Money(17_100))
    }

    @Test
    fun `현금 결제 시 2퍼센트 추가 할인이 적용된다`() {
        // 할인 적용된 금액 18,000 → 현금 2% → 17,640
        val paymentSystem = PaymentSystem(paymentMethod = Cash)

        val result = paymentSystem.pay(Money(18000), Point(0))

        assertThat(result.finalPrice).isEqualTo(Money(17_640))
    }
}
