package domain.payment

import domain.Money
import domain.movie.Movie
import domain.Point
import domain.reservation.Reservations
import domain.movie.RunningTime
import domain.screening.Screen
import domain.screening.Screening
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatNumber
import domain.seat.Seats
import domain.movie.ShowingPeriod
import domain.discount.MovieDayDiscountPolicy
import domain.discount.TimeDiscountPolicy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PaymentSystemTest {
    private val movie = Movie(
        title = "테스트 영화",
        runningTime = RunningTime(120),
        showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2025, 9, 1),
            endDate = LocalDate.of(2025, 9, 30)
        )
    )

    private val screen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.S))))

    private fun bothDiscountScreening() = Screening(
        movie = movie,
        startDateTime = LocalDateTime.of(LocalDate.of(2025, 9, 20), LocalTime.of(10, 0)),
        screen = screen
    )

    private fun normalScreening() = Screening(
        movie = movie,
        startDateTime = LocalDateTime.of(LocalDate.of(2025, 9, 15), LocalTime.of(14, 0)),
        screen = screen
    )

    @Test
    fun `무비데이 할인이 시간 할인보다 먼저 적용된다`() {
        // S등급 18,000 → 무비데이 10% → 16,200 → 시간할인 2,000 → 14,200 → 현금 2% → 13,916
        val paymentSystem = PaymentSystem(
            discountPolicies = listOf(MovieDayDiscountPolicy, TimeDiscountPolicy),
            paymentMethod = Cash
        )
        val reservations = Reservations()
            .addReservation(bothDiscountScreening().reserve(listOf(SeatNumber('A', 1))))

        val result = paymentSystem.pay(reservations, Point(0))

        assertThat(result).isEqualTo(Money(13_916))
    }

    @Test
    fun `포인트가 예매 금액에서 올바르게 차감된다`() {
        // S등급 18,000 → 포인트 2,000 차감 → 16,000 → 현금 2% → 15,680
        val paymentSystem = PaymentSystem(
            discountPolicies = emptyList(),
            paymentMethod = Cash
        )
        val reservations = Reservations()
            .addReservation(normalScreening().reserve(listOf(SeatNumber('A', 1))))

        val result = paymentSystem.pay(reservations, Point(2_000))

        assertThat(result).isEqualTo(Money(15_680))
    }

    @Test
    fun `포인트 차감 후 결제 수단 할인이 적용된다`() {
        // S등급 18,000 → 포인트 2,000 차감 → 16,000 → 카드 5% → 15,200
        val paymentSystem = PaymentSystem(
            discountPolicies = emptyList(),
            paymentMethod = Card
        )
        val reservations = Reservations()
            .addReservation(normalScreening().reserve(listOf(SeatNumber('A', 1))))

        val result = paymentSystem.pay(reservations, Point(2_000))

        assertThat(result).isEqualTo(Money(15_200))
    }

    @Test
    fun `신용카드 결제 시 5% 추가 할인이 적용된다`() {
        // S등급 18,000 → 카드 5% → 17,100
        val paymentSystem = PaymentSystem(
            discountPolicies = emptyList(),
            paymentMethod = Card
        )
        val reservations = Reservations()
            .addReservation(normalScreening().reserve(listOf(SeatNumber('A', 1))))

        val result = paymentSystem.pay(reservations, Point(0))

        assertThat(result).isEqualTo(Money(17_100))
    }

    @Test
    fun `현금 결제 시 2% 추가 할인이 적용된다`() {
        // S등급 18,000 → 현금 2% → 17,640
        val paymentSystem = PaymentSystem(
            discountPolicies = emptyList(),
            paymentMethod = Cash
        )
        val reservations = Reservations()
            .addReservation(normalScreening().reserve(listOf(SeatNumber('A', 1))))

        val result = paymentSystem.pay(reservations, Point(0))

        assertThat(result).isEqualTo(Money(17_640))
    }
}
