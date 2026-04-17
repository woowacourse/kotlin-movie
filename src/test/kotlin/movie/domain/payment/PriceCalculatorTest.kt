package movie.domain.payment

import movie.data.SeatsData
import movie.domain.amount.Point
import movie.domain.amount.Price
import movie.domain.discount.DiscountPolicy
import movie.domain.discount.PaymentDiscountPolicy
import movie.domain.movie.Movie
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FakeDiscountPolicy : DiscountPolicy {
    override fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price = price.minus(Price(1000))
}

class FakePaymentDiscountPolicy : PaymentDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        paymentMethod: PaymentMethod,
    ): Price = price.minus(Price(1000))
}

class PriceCalculatorTest {
    @Test
    fun `할인 정책, 포인트 차감, 결제 수단 할인 순서로 결제를 진행한다`() {
        // given
        val screening =
            Screening(
                id = 1L,
                screen = Screen(1, SeatsData.seats),
                screeningDateTime =
                    ScreeningDateTime(
                        LocalDateTime.of(2026, 1, 10, 10, 0),
                        LocalDateTime.of(2026, 1, 10, 12, 0),
                    ),
                reservedSeats =
                    ReservedSeats(
                        Seats(
                            setOf(
                                Seat("C", 1, SeatGrade.S),
                                Seat("C", 2, SeatGrade.S),
                            ),
                        ),
                    ),
            )

        val selectedSeats =
            SelectedSeats(
                Seats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                    ),
                ),
            )
        val movie = Movie(id = 1, title = "F1 더 무비", screenings = Screenings(listOf(screening)))

        // when
        val reservations =
            Reservations(
                listOf(
                    Reservation(movie, screening, selectedSeats),
                ),
            )

        val discountPolicy = FakeDiscountPolicy()
        val paymentDiscountPolicy = FakePaymentDiscountPolicy()
        val priceCalculator = PriceCalculator()

        val result =
            priceCalculator.calculate(
                reservations,
                discountPolicy,
                paymentDiscountPolicy,
                Point(1000),
                PaymentMethod.CreditCard,
            )

        assertThat(result.totalPrice).isEqualTo(Price(15000))
        assertThat(result.usedPoint).isEqualTo(Point(1000))
    }
}
