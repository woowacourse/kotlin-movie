package domain.discount

import domain.common.Money
import domain.payment.PaymentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountPolicyTest {

    @Test
    fun `의도한 티켓 할인이 순서대로 적용되어 예상된 금액을 할인한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = ticketDiscountPolicy.calculateDiscountResult(given, ticketContext)
        // then
        assertThat(result).isEqualTo(Money(7000))
    }

    @Test
    fun `의도한 결제 할인이 순서대로 적용되어 예상된 금액을 반환한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = totalDiscountPolicy.calculateDiscountResult(given, paymentContext)
        // then
        assertThat(result).isEqualTo(Money(9500))
    }

    @Test
    fun `카드 결제 시 할인 금액을 반환한다`() {
        // given
        val given = Money(10000)
        // when
        val result = PaymentDiscount().apply(given, paymentContext)
        // then
        assertThat(result).isEqualTo(Money(500))
    }

    @Test
    fun `현금 결제 시 할인 금액을 반환한다`() {
        // given
        val given = Money(10000)
        // when
        val result = PaymentDiscount().apply(given, PaymentDiscountContext(PaymentType.CASH))
        // then
        assertThat(result).isEqualTo(Money(200))
    }

    @Test
    fun `무비데이 조건에 해당하는 경우 할인 금액을 반환한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = MoviedayDiscount().apply(given, ticketContext)
        // then
        assertThat(result).isEqualTo(Money(1000))
    }

    @Test
    fun `특정 시간대 조건에 해당하는 경우 할인 금액을 반환한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = TimeDiscount().apply(given, ticketContext)
        // then
        assertThat(result).isEqualTo(Money(2000))
    }

    private val ticketDiscountPolicy = TicketDiscountPolicy(
        strategies = listOf(
            MoviedayDiscount(),
            TimeDiscount(),
        )
    )

    private val totalDiscountPolicy = TotalDiscountPolicy(
        strategies = listOf(
            PaymentDiscount(),
        )
    )

    private val ticketContext = TicketDiscountContext(
        dateTime = LocalDateTime.of(2026, 4, 10, 10, 0)
    )

    private val paymentContext = PaymentDiscountContext(
        paymentType = PaymentType.CREDIT_CARD
    )

}
