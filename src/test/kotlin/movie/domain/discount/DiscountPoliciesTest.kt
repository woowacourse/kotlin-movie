package movie.domain.discount

import movie.domain.amount.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountPoliciesTest {
    @Test
    fun `무비데이와 시간 할인이 동시에 적용되면 무비데이 할인(비율)이 먼저 적용되고, 이어서 시간 조건 할인(정액)이 적용된다`() {
        // given
        val movieDayDiscount = MovieDayDiscount()
        val timeDiscount = TimeDiscount()
        val discountPolicies = DiscountPolicies(listOf(movieDayDiscount), listOf(timeDiscount))

        // when
        val result =
            discountPolicies.applyDiscount(
                Money(10000),
                LocalDateTime.of(2026, 1, 10, 10, 0),
            )

        // then
        assertThat(result).isEqualTo(Money(7000))
    }
}
