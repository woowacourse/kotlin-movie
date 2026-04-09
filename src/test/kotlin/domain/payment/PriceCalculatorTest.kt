package domain.payment

import domain.amount.Money
import domain.amount.Point
import domain.discount.DiscountPolicies
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount
import domain.screening.ScreeningDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class PriceCalculatorTest {
    @Test
    fun `할인 정책, 포인트 차감, 결제 수단 할인 순서로 결제를 진행한다`() {
        // given
        val ticketPrice = Money(10000)
        val discountPolicies =
            DiscountPolicies(
                listOf(MovieDayDiscount()),
                listOf(TimeDiscount()),
            )
        val point = Point(1000)
        val screenDateTime =
            ScreeningDateTime(
                LocalDate.of(2026, 1, 10),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
            )
        val priceCalculator =
            PriceCalculator(
                ticketPrice,
                screenDateTime,
                discountPolicies,
                point,
                CreditCard(),
            )

        // when
        val result = priceCalculator.calculate()

        // then
        assertThat(result.price).isEqualTo(Money(5700))
        assertThat(result.usagePoint).isEqualTo(Point(1000))
    }
}
