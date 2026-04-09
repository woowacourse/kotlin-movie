package model.payment

import model.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardTest {
    @Test
    fun `신용카드 결제 시 5% 할인이 적용된다`() {
        val discount = Card.calculateDiscountAmount(Money(50_000))
        assertThat(discount).isEqualTo(Money(2_500))
    }
}
