package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
import movie.domain.movie.MovieTitle
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.ScreenId
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.ScreeningSlot
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
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
                movie = MovieTitle("토이 스토리"),
                slot =
                    ScreeningSlot(
                        Screen(ScreenId(1), Seats.createDefault()),
                        ScreeningDateTime(
                            LocalDate.of(2026, 1, 10),
                            LocalTime.of(10, 0),
                            LocalTime.of(12, 0),
                        ),
                    ),
                reservatedSeats =
                    ReservatedSeats(
                        listOf(
                            Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                            Seat(SeatRow("C"), SeatColumn(2), SeatGrade.S),
                        ),
                    ),
            )

        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val reservations =
            Reservations(
                listOf(
                    Reservation(screening, selectedSeats),
                ),
            )

        val priceCalculator =
            PriceCalculator(
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
            )

        val result =
            priceCalculator.calculate(
                reservations,
                Point(1000),
                CreditCard(),
            )

        assertThat(result.totalPrice).isEqualTo(Money(12540))
        assertThat(result.usedPoint).isEqualTo(Point(1000))
    }
}
