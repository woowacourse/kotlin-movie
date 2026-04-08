package domain

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

enum class PaymentMethod {
    CARD,
    CASH,
}

object DiscountPolicy {
    private val movieDay = listOf<Int>(10, 20, 30)
    private val noneDiscountTimeBoundary =
        LocalTime(11, 0) to LocalTime(20, 0)

    const val MOVIE_DAY_DISCOUNT_PERCENT = 0.1
    const val NONE_DISCOUNT_PERCENT = 0.0

    const val TIME_DISCOUNT_PRICE = 2000
    const val NONE_DISCOUNT_PRICE = 0

    const val CARD_DISCOUNT_PERCENT = 0.05
    const val CASH_DISCOUNT_PERCENT = 0.02
    fun movieDayDiscount(date: LocalDateTime): Double {
        if (movieDay.contains(date.day)) {
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

    fun paymentDiscount(method: PaymentMethod): Double {
        return when (method) {
            PaymentMethod.CARD -> CARD_DISCOUNT_PERCENT
            PaymentMethod.CASH -> CASH_DISCOUNT_PERCENT
        }
    }
}

class DiscountPolicyTest {
    @Test
    fun `매월 10일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 10일이다.
        val date = LocalDateTime(2026, 4, 10, 14, 0)

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT, result)
    }

    @Test
    fun `매월 20일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 20일이다.
        val date = LocalDateTime(2026, 4, 20, 14, 0)

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT, result)
    }

    @Test
    fun `매월 30일에 상영되는 영화는 10% 할인된다`() {
        // given : 날짜가 30일이다.
        val date = LocalDateTime(2026, 4, 30, 14, 0)

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.1이 반환된다.
        assertEquals(DiscountPolicy.MOVIE_DAY_DISCOUNT_PERCENT, result)
    }

    @Test
    fun `무비데이가 아닌 날에는 무비데이 할인이 적용되지 않는다`() {
        // given : 날짜가 1일이다.
        val date = LocalDateTime(2026, 4, 1, 14, 0)

        // when : 할인율을 계산하면
        val result = DiscountPolicy.movieDayDiscount(date)

        // then : 0.0이 반환된다.
        assertEquals(DiscountPolicy.NONE_DISCOUNT_PERCENT, result)
    }

    @Test
    fun `오전 11시 이전에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오전 11시 이전 시간이 주어진다
        val time: LocalDateTime = LocalDateTime(2026, 4, 8, 10, 0)

        // when : 할인액을 계산하면
        val result = DiscountPolicy.showTimeDiscount(time)

        // then : 2000이 반환된다.
        assertEquals(DiscountPolicy.TIME_DISCOUNT_PRICE, result)
    }

    @Test
    fun `오후 8시 이후에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오후 8시 이후 시간이 주어진다
        val time: LocalDateTime = LocalDateTime(2026, 4, 8, 21, 0)

        // when : 할인액을 계산하면
        val result = DiscountPolicy.showTimeDiscount(time)

        // then : 2000이 반환된다.
        assertEquals(DiscountPolicy.TIME_DISCOUNT_PRICE, result)
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

    @Test
    fun `신용카드 결제 시 5% 할인된다`() {
        // given : 결제수단으로 신용카드가 제시된다
        val method = PaymentMethod.CARD

        // when : 할인율을 계산하면
        val result = DiscountPolicy.paymentDiscount(method)

        // then : 0.05가 반환된다.
        assertEquals(DiscountPolicy.CARD_DISCOUNT_PERCENT, result)
    }

    @Test
    fun `현금 결제 시 2% 할인된다`() {
        // given : 결제수단으로 현금이 제시된다
        val method = PaymentMethod.CASH

        // when : 할인율을 계산하면
        val result = DiscountPolicy.paymentDiscount(method)

        // then : 0.02가 반환된다.
        assertEquals(DiscountPolicy.CASH_DISCOUNT_PERCENT, result)
    }
}
