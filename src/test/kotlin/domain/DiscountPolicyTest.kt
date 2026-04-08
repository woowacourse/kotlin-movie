package domain

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object DiscountPolicy {
    private val movieDay = listOf<Int>(10, 20, 30)
    private val noneDiscountTimeBoundary =
        LocalTime(11, 0) to LocalTime(20, 0)

    const val MOVIE_DAY_DISCOUNT_PERCENT = 0.1
    const val NONE_DISCOUNT_PERCENT = 0.0

    const val TIME_DISCOUNT_PRICE = 2000
    const val NONE_DISCOUNT_PRICE = 0

    fun movieDayDiscount(date: Int): Double {
        if (movieDay.contains(date)) {
            return MOVIE_DAY_DISCOUNT_PERCENT
        }
        return NONE_DISCOUNT_PERCENT
    }

    fun showTimeDiscount(date: LocalDateTime): Int {
        if (date.time > noneDiscountTimeBoundary.first && date.time < noneDiscountTimeBoundary.second) {
            return NONE_DISCOUNT_PRICE
        }
        return TIME_DISCOUNT_PRICE
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

    @Test
    fun `오전 11시 이전에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오전 11시 이전 시간이 주어진다
        val time: LocalDateTime = LocalDateTime(2026, 4, 8, 10, 0)

        // when : 할인액을 계산하면
        val result = DiscountPolicy.showTimeDiscount(time)

        // then : 2000이 반환된다.
        assertEquals(result, DiscountPolicy.TIME_DISCOUNT_PRICE)
    }

    @Test
    fun `오후 8시 이후에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오후 8시 이후 시간이 주어진다
        val time: LocalDateTime = LocalDateTime(2026, 4, 8, 21, 0)

        // when : 할인액을 계산하면
        val result = DiscountPolicy.showTimeDiscount(time)

        // then : 2000이 반환된다.
        assertEquals(result, DiscountPolicy.TIME_DISCOUNT_PRICE)
    }

    @Test
    fun `오전 11시 ~ 오후 8시 사이에 시작하는 상영은 시간 할인이 적용되지 않는다`() {
        // given : 오후 4시 시간이 주어진다
        val time: LocalDateTime = LocalDateTime(2026, 4, 8, 16, 0)

        // when : 할인액을 계산하면
        val result = DiscountPolicy.showTimeDiscount(time)

        // then : 2000이 반환된다.
        assertEquals(DiscountPolicy.NONE_DISCOUNT_PRICE, result)
    }
}
