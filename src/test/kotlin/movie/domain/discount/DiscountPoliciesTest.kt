package movie.domain.discount

import movie.domain.amount.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FakePercentageDiscount : PercentageDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price = price.percentOf(90)
}

class FakeFixedAmountDiscount : FixedAmountDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price = price.minus(Price(2000))
}

class DiscountPoliciesTest {
    @Test
    fun `비율 할인 정책이 먼저 적용된 후 정액 할인 정책이 적용된다`() {
        // given
        val discountPolicies =
            DiscountPolicies(
                listOf(FakePercentageDiscount()),
                listOf(FakeFixedAmountDiscount()),
            )

        // when
        val result =
            discountPolicies.applyDiscount(
                Price(10000),
                LocalDateTime.of(2026, 1, 10, 10, 0),
            )

        // then
        assertThat(result).isEqualTo(Price(7000))
    }
}
