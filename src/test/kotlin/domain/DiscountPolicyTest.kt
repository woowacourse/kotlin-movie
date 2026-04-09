package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class DiscountPolicyTest {

    /*
    - [ ] 카드 결제 시 할인 금액을 반환한다
- [ ] 현금 결제 시 할인 금액을 반환한다
- [ ] 무비데이 조건에 해당하는 경우 할인 금액을 반환한다
- [ ] 특정 시간대 조건에 해당하는 경우 할인 금액을 반환한다
     */

    @Test
    fun `카드 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = DiscountPolicy().cardDiscount(given)

        assertThat(result).isEqualTo(Money(500))
    }

    @Test
    fun `현금 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = DiscountPolicy().cashDiscount(given)

        assertThat(result).isEqualTo(Money(200))
    }

    @Test
    fun `무비데이 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = DiscountPolicy().movieDayDiscount(
            money = given,
            date = LocalDate.of(2026, 4, 10)
        )

        assertThat(result).isEqualTo(Money(1000))
    }

    @Test
    fun `특정 시간대 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = DiscountPolicy().timeDiscount(
            money = given,
            time = LocalTime.of(10, 0)
        )

        assertThat(result).isEqualTo(Money(2000))
    }
}
