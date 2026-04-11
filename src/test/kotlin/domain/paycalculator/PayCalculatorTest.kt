package domain.paycalculator

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.MovieDayCondition
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.TimeCondition
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.point.Point
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class PayCalculatorTest {
    private lateinit var payCalculator: PayCalculator
    private lateinit var reservations: List<Reservation>

    @BeforeEach
    fun setUp() {
        payCalculator = PayCalculator()
        reservations = createMockReservation()
    }

    @Test
    fun `예약 목록을 바탕으로 초기 총 금액을 계산한다`() {
        val amount = payCalculator.calculateInitPrice(reservations)

        assertThat(amount).isEqualTo(Money(26000))
    }

    @Test
    fun `계산된 총 금액에 시간 및 날짜 할인을 적용한다`() {
        val initPrice = Money(26000)
        val timeCondition =
            TimeCondition(
                beforeTime = LocalTime.of(11, 0),
                afterTime = LocalTime.of(20, 0),
            )
        val timeDiscountPolicy =
            TimeDiscountPolicy(
                condition = timeCondition,
                discountAmount = Money(2000),
            )

        val movieDayCondition =
            MovieDayCondition(
                condition = listOf(10, 20, 30),
            )
        val movieDayDiscountPolicy =
            MovieDayDiscountPolicy(
                condition = movieDayCondition,
                discountRate = 0.9,
            )
        val discountPolicies = listOf(movieDayDiscountPolicy, timeDiscountPolicy)

        val amount = payCalculator.calculateTimeDiscountedPrice(initPrice, reservations, discountPolicies)

        assertThat(amount).isEqualTo(Money(21400))
    }

    @Test
    fun `포인트를 사용하면 사용한 만큼 금액이 차감된다`() {
        val price = Money(20000)
        val point = Point(2000)

        val result = payCalculator.usePoint(price, point)

        assertThat(result).isEqualTo(Money(18000))
    }

    @Test
    fun `현금 결제를 선택하면 해당 정책에 따른 할인이 적용된다`() {
        val price = Money(10000)
        val cashPolicy =
            CashDiscountPolicy(
                discountRate = 0.98,
            )

        val result = payCalculator.usePayMethod(price, cashPolicy)

        assertThat(result).isEqualTo(Money(9800))
    }

    @Test
    fun `카드 결제를 선택하면 해당 정책에 따른 할인이 적용된다`() {
        val price = Money(10000)
        val cardPolicy =
            CardDiscountPolicy(
                discountRate = 0.95,
            )

        val result = payCalculator.usePayMethod(price, cardPolicy)

        assertThat(result).isEqualTo(Money(9500))
    }

    private fun createMockReservation(): List<Reservation> =
        listOf(
            Reservation(
                movie =
                    Movie(
                        title = Title("신바드의 모험"),
                        runningTime = RunningTime(120),
                        screeningPeriod =
                            ScreeningPeriod(
                                startDate = LocalDate.of(2026, 4, 1),
                                endDate = LocalDate.of(2026, 4, 30),
                            ),
                    ),
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
                seats =
                    listOf(
                        Seat("A1"),
                        Seat("B1"),
                    ),
            ),
        )
}
