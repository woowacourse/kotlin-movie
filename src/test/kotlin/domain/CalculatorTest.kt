package domain

import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object Calculator {
    fun applyDiscountByMovie(
        price: Int,
        date: LocalDateTime,
    ): Int {
        val discountedPrice = applyMovieDayDiscount(price, date)

        return discountedPrice
    }

    fun applyMovieDayDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return ((1 - DiscountPolicy.movieDayDiscount(date)) * price).toInt()
    }

    fun applyTimeDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return price - DiscountPolicy.showTimeDiscount(date)
    }
}

class CalculatorTest {
    @Test
    fun `무비데이에 상영되는 영화는 10% 할인이 적용된다`() {
        // given : 날짜가 10일이고, 영화가 16_000원이다.
        val price = 16_000
        val date = LocalDateTime(2026, 4, 10, 14, 0)

        // when : 할인율을 적용하면
        val result = Calculator.applyMovieDayDiscount(price, date)

        // then : 14_400원이 반환된다.
        assertEquals(14_400, result)
    }

    @Test
    fun `할인 시간에 상영되는 영화는 2000원 할인이 적용된다`() {
        // given : 시간이 10시이고, 영화가 16_000원이다.
        val price = 16_000
        val date = LocalDateTime(2026, 4, 10, 10, 0)

        // when : 할인액을 적용하면
        val result = Calculator.applyTimeDiscount(price, date)

        // then : 14_000원이 반환된다.
        assertEquals(14_000, result)
    }
}
