package domain

import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object Calculator {

    fun subtractUserPoint(
        price: Int,
        user: User,
        subtractPoint: Long,
    ): Long {
        user.discountPoint(subtractPoint)
        return price - subtractPoint
    }

    fun calculateByMovie(
        price: Int,
        date: LocalDateTime,
    ): Int {
        var discountedPrice = applyMovieDayDiscount(price, date)
        discountedPrice = applyTimeDiscount(discountedPrice, date)

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

    fun applyPaymentDiscount(
        price: Int,
        method: PaymentMethod,
    ): Int {
        return ((1 - DiscountPolicy.paymentDiscount(method)) * price).toInt()
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

    @Test
    fun `무비데이도 아니고 할인 시간도 아닌 영화는 할인이 적용되지 않는다`() {
        // given : 날짜가 11일이고 시간이 14시이고, 영화가 16_000원이다.
        val price = 16_000
        val date = LocalDateTime(2026, 4, 11, 14, 0)

        // when : 할인을 적용하면
        val result = Calculator.calculateByMovie(price, date)

        // then : 16_000원이 반환된다.
        assertEquals(16_000, result)
    }

    @Test
    fun `무비데이 할인과 시간 할인이 순서대로 적용된다`() {
        // given : 날짜가 10일이고 시간이 10시이고, 영화가 16_000원이다.
        val price = 16_000
        val date = LocalDateTime(2026, 4, 10, 10, 0)

        // when : 할인을 적용하면
        val result = Calculator.calculateByMovie(price, date)

        // then : 12_400원이 반환된다.
        assertEquals(12_400, result)
    }

    @Test
    fun `포인트가 예매 금액에서 직접 차감된다`() {
        // given : user는 1000 포인트를 가지고 있다.
        val user = User(1)
        val price = 16_000

        // when : 사용 포인트가 1000이면
        val result = Calculator.subtractUserPoint(
            price = price,
            user = user,
            subtractPoint = 1000,
        )

        // then : user의 포인트는 0이 되고 15_000원이 반환된다.
        assertEquals(15_000, result)
        assertEquals(0, user.point)
    }

    @Test
    fun `결제 수단에 따라 할인이 적용된다`() {
        // given : 결제 수단은 카드이다.
        val price = 16_000
        val method = PaymentMethod.CARD

        // when : 할인을 적용하면
        val result = Calculator.applyPaymentDiscount(price, method)

        // then : 15_200원이 반환된다.
        assertEquals(15_200, result)
    }
}
