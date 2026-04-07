package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object DiscountPolicy {
    private val movieDay = listOf<Int>(10, 20, 30)
    const val MOVIE_DAY_DISCOUNT_PERCENT = 0.1
    const val NONE_DISCOUNT_PERCENT = 0.0

    fun movieDayDiscount(date: Int): Double {
        if (movieDay.contains(date)) {
            return MOVIE_DAY_DISCOUNT_PERCENT
        }
        return NONE_DISCOUNT_PERCENT
    }
}

class DiscountPolicyTest {
    @Test
    fun `매월 10일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 10일이다.
        val date = 10

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(result, DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT)
    }

    @Test
    fun `매월 20일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 20일이다.
        val date = 20

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(result, DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT)
    }

    @Test
    fun `매월 30일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 30일이다.
        val date = 30

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(result, DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT)
    }

    @Test
    fun `무비데이가 아닌 날에는 무비데이 할인이 적용되지 않는다`() {
        // given : 날짜가 1일이다.
        val date = 1

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.0이 반환된다.
        assertEquals(result, DiscountPolicy.NONE_DISCOUNT_PERCENT)
    }
}
