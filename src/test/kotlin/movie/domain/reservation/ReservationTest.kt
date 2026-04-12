package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.TimeDiscount
import movie.domain.movie.MovieTitle
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

class ReservationTest {
    @Test
    fun `예약은 할인 조건에 해당하지 않으면 원가를 반환한다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                MovieTitle("F1 더 무비"),
                ScreeningSlot(
                    Screen(ScreenId(1), Seats.createDefault()),
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(13, 0),
                        LocalTime.of(15, 0),
                    ),
                ),
                ReservatedSeats(
                    listOf(
                        Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                        Seat(SeatRow("C"), SeatColumn(2), SeatGrade.S),
                        Seat(SeatRow("E"), SeatColumn(1), SeatGrade.A),
                    ),
                ),
            )
        val reservation = Reservation(screening, selectedSeats)

        // when
        val discountPolicies =
            DiscountPolicies(
                percentagePolicies = emptyList(),
                fixedPolicies = listOf(TimeDiscount()),
            )

        // when
        val price = reservation.calculateDiscountedPrice(discountPolicies)

        // then
        assertThat(price).isEqualTo(Money(30000))
    }

    @Test
    fun `예약은 자기 상영 정보를 기반으로 할인된 가격을 계산할 수 있다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                MovieTitle("F1 더 무비"),
                ScreeningSlot(
                    Screen(ScreenId(1), Seats.createDefault()),
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                    ),
                ),
                ReservatedSeats(
                    listOf(
                        Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                        Seat(SeatRow("C"), SeatColumn(2), SeatGrade.S),
                        Seat(SeatRow("E"), SeatColumn(1), SeatGrade.A),
                    ),
                ),
            )
        val reservation = Reservation(screening, selectedSeats)
        val discountPolicies =
            DiscountPolicies(
                percentagePolicies = emptyList(),
                fixedPolicies = listOf(TimeDiscount()),
            )

        // when
        val discountedPrice = reservation.calculateDiscountedPrice(discountPolicies)

        // then
        assertThat(discountedPrice).isEqualTo(Money(28000))
    }
}
