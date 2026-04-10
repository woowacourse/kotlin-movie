package movie.domain.payment

import movie.domain.amount.Point
import movie.domain.amount.Price
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
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
import java.time.LocalDate
import java.time.LocalTime

class PriceCalculatorTest {
    @Test
    fun `할인 정책, 포인트 차감, 결제 수단 할인 순서로 결제를 진행한다`() {
        // given
        val screening =
            Screening(
                screen = Screen(1, Seats.createDefault()),
                screeningDateTime =
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 10),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                    ),
                reservedSeats =
                    ReservedSeats(
                        setOf(
                            Seat("C", 1, SeatGrade.S),
                            Seat("C", 2, SeatGrade.S),
                        ),
                    ),
            )

        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("C", 1, SeatGrade.S),
                ),
            )
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))

        // when
        val reservations =
            Reservations(
                listOf(
                    Reservation(movie, screening, selectedSeats),
                ),
            )

        val discountPolicies =
            DiscountPolicies(
                percentagePolicies = listOf(MovieDayDiscount()),
                fixedPolicies = listOf(TimeDiscount()),
            )

        val priceCalculator = PriceCalculator()

        val result =
            priceCalculator.calculate(
                discountPolicies,
                reservations,
                Point(1000),
                CreditCard(),
            )

        assertThat(result.totalPrice).isEqualTo(Price(12540))
        assertThat(result.usedPoint).isEqualTo(Point(1000))
    }
}
