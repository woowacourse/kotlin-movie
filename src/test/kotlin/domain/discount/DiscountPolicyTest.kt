package domain.payment

import domain.common.Money
import domain.discount.DiscountContext
import domain.discount.DiscountPolicy
import domain.discount.MovieDayDiscount
import domain.discount.PaymentDiscount
import domain.discount.TimeDiscount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DiscountPolicyTest {

    @Test
    fun `의도한 할인 정책이 순서대로 적용되어 예상된 금액을 할인한다`() {
        // given
        val given = Money(10_000)
        val context = DiscountContext(
            dateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            paymentType = PaymentType.CREDIT_CARD
        )
        // when
        val result = discountPolicy.calculateDiscountResult(given, Point(150), context)
        // then
        assertThat(result).isEqualTo(Money(6500))
    }
    @Test
    fun `카드 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val strategy = PaymentDiscount()
        val context = DiscountContext(
            paymentType = PaymentType.CREDIT_CARD,
            dateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            )
        val result = strategy.apply(given, context)
        assertThat(result).isEqualTo(Money(500))
    }

    fun `현금 결제 시 할인 금액을 반환한다`() {
        val given = Money(10000)
        val strategy = PaymentDiscount()
        val context = DiscountContext(
            paymentType = PaymentType.CASH,
            dateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
        )
        val result = strategy.apply(given, context)
        assertThat(result).isEqualTo(Money(200))
    }

    @Test
    fun `무비데이 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val strategy = MovieDayDiscount()
        val context = DiscountContext(
            dateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            paymentType = PaymentType.CREDIT_CARD
        )

        val result = strategy.apply(given, context)
        assertThat(result).isEqualTo(Money(1000))
    }

    @Test
    fun `특정 시간대 조건에 해당하는 경우 할인 금액을 반환한다`() {
        val given = Money(10000)
        val strategy = TimeDiscount()
        val context = DiscountContext(
            dateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            paymentType = PaymentType.CREDIT_CARD
        )

        val result = strategy.apply(given, context)
        assertThat(result).isEqualTo(Money(2000))
    }

    private val discountPolicy = DiscountPolicy(
        strategies = listOf(
            MovieDayDiscount(),
            TimeDiscount(),
            PaymentDiscount(),
        )
    )
}
