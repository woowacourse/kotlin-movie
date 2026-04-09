package movie.domain.discount

import movie.domain.amount.Money
import movie.domain.screening.ScreeningDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountTest {
    @Test
    fun `오전 11시 이전 상영은 2000원 할인된다`() {
        // given
        val timeDiscount = TimeDiscount()
        val money = Money(10000)
        val screenDateTime = ScreeningDateTime(LocalDate.of(2026, 1, 10), LocalTime.of(8, 0), LocalTime.of(9, 0))

        // when
        val result = timeDiscount.applyDiscount(money, screenDateTime)

        // then
        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `오후 8시 이후 상영은 2000원 할인된다`() {
        // given
        val timeDiscount = TimeDiscount()
        val money = Money(10000)
        val screenDateTime = ScreeningDateTime(LocalDate.of(2026, 1, 10), LocalTime.of(20, 10), LocalTime.of(22, 0))

        // when
        val result = timeDiscount.applyDiscount(money, screenDateTime)

        // then
        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `시간 할인이 적용되지 않는 상영은 할인이 적용되지 않는다`() {
        // given
        val timeDiscount = TimeDiscount()
        val money = Money(10000)
        val screenDateTime = ScreeningDateTime(LocalDate.of(2026, 1, 10), LocalTime.of(12, 0), LocalTime.of(14, 0))

        // when
        val result = timeDiscount.applyDiscount(money, screenDateTime)

        // then
        assertThat(result).isEqualTo(Money(10000))
    }
}
