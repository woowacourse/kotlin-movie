package domain

import domain.discount.MovieDayEvent
import domain.discount.PaymentDiscount
import domain.discount.TheaterEventDiscount
import domain.discount.TimeEvent
import domain.payment.Money
import domain.payment.PaymentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountPolicyTest {
    private val paymentDiscountPolicy = PaymentDiscount()
    private val movieDayEvent = MovieDayEvent()
    private val timeEvent = TimeEvent()

    private val eventDiscountPolicy = TheaterEventDiscount()

    @Test
    fun `카드 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = paymentDiscountPolicy.discount(given, PaymentType.CREDIT_CARD)

        assertThat(result).isEqualTo(Money(9500))
    }

    @Test
    fun `현금 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = paymentDiscountPolicy.discount(given, PaymentType.CASH)

        assertThat(result).isEqualTo(Money(9800))
    }

    @Test
    fun `무비데이 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = movieDayEvent.discount(given, LocalDateTime.of(2026, 4, 10, 10, 0))

        assertThat(result).isEqualTo(Money(9000))
    }

    @Test
    fun `특정 시간대 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = timeEvent.discount(given, LocalDateTime.of(2026, 4, 10, 10, 0))

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `이벤트 할인을 순서대로 적용하여 할인 금액을 반환한다`() {
        val given = Money(10000)
        val result = eventDiscountPolicy.discount(given, LocalDateTime.of(2026, 4, 10, 10, 0))

        assertThat(result).isEqualTo(Money(7000))
    }
}
