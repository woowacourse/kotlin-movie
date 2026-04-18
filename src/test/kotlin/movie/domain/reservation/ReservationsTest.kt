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
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `예매를 추가할 수 있다`() {
        // given
        val reservationData = reservationFixtures()
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                id = 103L,
                MovieTitle("토이 스토리"),
                ScreeningSlot(
                    Screen(ScreenId(1), Seats.createDefault()),
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 0),
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
        val addedReservation = Reservation(screening, selectedSeats)
        val reservations = Reservations(reservationData)

        // when
        val result = reservations.add(addedReservation)

        // then
        assertThat(result).isEqualTo(Reservations(reservationData + addedReservation))
    }

    @Test
    fun `시간이 겹치는 예매를 추가할 수 없다`() {
        val reservationData = reservationFixtures()
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                id = 103L,
                MovieTitle("토이 스토리"),
                ScreeningSlot(
                    Screen(ScreenId(1), Seats.createDefault()),
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(13, 0),
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
        val addedReservation = Reservation(screening, selectedSeats)
        val reservations = Reservations(reservationData)

        val exception =
            assertThrows<IllegalArgumentException> {
                reservations.add(addedReservation)
            }
        assert(exception.message == "상영 시간이 겹치는 예매가 존재합니다.")
    }

    @Test
    fun `전체 결제 대상 금액은 예매별 할인 적용 금액의 합이다`() {
        // given
        val reservationData = reservationFixtures()
        val reservations = Reservations(reservationData)
        val discountPolicies =
            DiscountPolicies(
                percentagePolicies = emptyList(),
                fixedPolicies = listOf(TimeDiscount()),
            )

        // then
        assertThat(reservations.totalPrice(discountPolicies)).isEqualTo(Money(58000))
    }

    private fun reservationFixtures(): List<Reservation> {
        val screen = Screen(ScreenId(1), Seats.createDefault())
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )

        val screening1 =
            Screening(
                id = 101L,
                MovieTitle("토이 스토리"),
                ScreeningSlot(
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                    ),
                ),
                ReservatedSeats(emptyList()),
            )

        val screening2 =
            Screening(
                id = 102L,
                MovieTitle("F1 더 무비"),
                ScreeningSlot(
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2026, 1, 1),
                        LocalTime.of(14, 0),
                        LocalTime.of(16, 0),
                    ),
                ),
                ReservatedSeats(emptyList()),
            )

        return listOf(
            Reservation(screening1, selectedSeats),
            Reservation(screening2, selectedSeats),
        )
    }
}
