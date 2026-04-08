package domain.discount

import domain.amount.Money
import domain.screening.ScreeningDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class MovieDayDiscountTest {
    @Test
    fun `무비데이이면 10프로가 할인된다`() {
        // given
        val movieDayDiscount = MovieDayDiscount()
        val money = Money(10000)
        val screenDateTime = ScreeningDateTime(LocalDate.of(2026, 1, 10), LocalTime.of(10, 0), LocalTime.of(12, 0))

        // when
        val result = movieDayDiscount.applyDiscount(money, screenDateTime)

        // then
        assertThat(result).isEqualTo(Money(9000))
    }

    @Test
    fun `무비데이가 아닌 날에는 할인이 적용되지 않는다`() {
        // given
        val movieDayDiscount = MovieDayDiscount()
        val money = Money(10000)
        val screenDateTime = ScreeningDateTime(LocalDate.of(2026, 1, 11), LocalTime.of(10, 0), LocalTime.of(12, 0))

        // when
        val result = movieDayDiscount.applyDiscount(money, screenDateTime)

        // then
        assertThat(result).isEqualTo(Money(10000))
    }
}
