package domain.discountpolicy

import domain.money.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayMethodPolicyMethodTest {
    val cardDiscountPolicy = CardDiscountPolicy()
    val cashDiscountPolicy = CashDiscountPolicy()

    @Test
    fun `카드 결제를 선택하면 5% 할인이 적용된다` () {
        val amount = Money(10000)

        assertThat(cardDiscountPolicy.applyDiscount(amount)).isEqualTo(Money(9500))
    }

    @Test
    fun `현금 결제를 선택하면 3% 할인이 적용된다`() {
        val amount = Money(10000)

        assertThat(cashDiscountPolicy.applyDiscount(amount)).isEqualTo(Money(9800))
    }
}