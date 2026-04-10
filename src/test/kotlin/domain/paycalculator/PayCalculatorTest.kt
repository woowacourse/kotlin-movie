package domain.paycalculator

import domain.discountpolicy.PayMethod
import domain.money.Money
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class PayCalculatorTest {
    @Test
    fun `PayCalculator가 생성될 때 예약 목록의 좌석을 바탕으로 총 금액이 계산된다`() {
        val payCalculator = PayCalculator(createMockReservation())

        val amount = payCalculator.getTotalPrice()

        assertThat(amount).isEqualTo(Money(26000))
    }

    @Test
    fun `계산된 총 금액에 조건에 따른 할인을 적용한다`() {
        val payCalculator = PayCalculator(createMockReservation())

        payCalculator.calculateTotalPrice()

        val amount = payCalculator.getTotalPrice()

        assertThat(amount).isEqualTo(Money(21400))
    }

    @Test
    fun `포인트를 사용하면 사용한 만큼 금액이 차감된다`() {
        val payCalculator = PayCalculator(createMockReservation())

        payCalculator.calculateTotalPrice()

        payCalculator.usePoint(2000)

        val amount = payCalculator.getTotalPrice()

        assertThat(amount).isEqualTo(Money(19400))
    }

    @Test
    fun `현금 결제를 선택하면 2% 할인이 적용된다`() {
        val payCalculator = PayCalculator(createMockReservation())

        payCalculator.calculateTotalPrice()

        payCalculator.usePoint(2000)

        payCalculator.getTotalPrice()

        payCalculator.pay(PayMethod.CASH)

        val amount = payCalculator.getTotalPrice()

        assertThat(amount).isEqualTo(Money(19012))
    }

    @Test
    fun `카드 결제를 선택하면 5% 할인이 적용된다`() {
        val payCalculator = PayCalculator(createMockReservation())

        payCalculator.calculateTotalPrice()

        payCalculator.usePoint(2000)

        payCalculator.getTotalPrice()

        payCalculator.pay(PayMethod.CASH)

        val amount = payCalculator.getTotalPrice()

        assertThat(amount).isEqualTo(Money(19012))
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
                                startDate =
                                    LocalDate.of(
                                        2026,
                                        4,
                                        1,
                                    ),
                                endDate =
                                    LocalDate.of(
                                        2026,
                                        4,
                                        30,
                                    ),
                            ),
                    ),
                screenTime =
                    ScreenTime(
                        startTime =
                            LocalTime.of(
                                11,
                                0,
                            ),
                        endTime =
                            LocalTime.of(
                                13,
                                0,
                            ),
                        screeningDate =
                            LocalDate.of(
                                2026,
                                4,
                                10,
                            ),
                    ),
                seats =
                    listOf<Seat>(
                        Seat(
                            "A1",
                        ),
                        Seat(
                            "B1",
                        ),
                    ),
            ),
        )
}
