package domain.payment

import domain.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CashTest {
    @Test
    fun `현금 결제 시 2% 할인이 적용된다`() {
        val discount = Cash.calculateDiscountAmount(Money(50_000))
        assertThat(discount).isEqualTo(Money(1_000))
    }
}