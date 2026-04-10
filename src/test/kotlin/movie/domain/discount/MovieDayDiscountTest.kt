package movie.domain.discount

import movie.domain.amount.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class MovieDayDiscountTest {
    @Test
    fun `무비데이이면 10프로가 할인된다`() {
        // given
        val movieDayDiscount = MovieDayDiscount()
        val price = Price(10000)
        val date = LocalDate.of(2026, 1, 10)
        val startTime = LocalTime.of(10, 0)

        // when
        val result = movieDayDiscount.applyDiscount(price, date.atTime(startTime))

        // then
        assertThat(result).isEqualTo(Price(9000))
    }

    @Test
    fun `무비데이가 아닌 날에는 할인이 적용되지 않는다`() {
        // given
        val movieDayDiscount = MovieDayDiscount()
        val price = Price(10000)
        val date = LocalDate.of(2026, 1, 11)
        val startTime = LocalTime.of(10, 0)

        // when
        val result = movieDayDiscount.applyDiscount(price, date.atTime(startTime))

        // then
        assertThat(result).isEqualTo(Price(10000))
    }
}
