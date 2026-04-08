package domain.reservation

import domain.amount.Money
import domain.screening.Screen
import domain.screening.Screening
import domain.screening.ScreeningDateTime
import domain.seat.ReservatedSeats
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.Seats
import domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    @Test
    fun `선택한 좌석들의 총 가격을 계산할 수 있다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                1,
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservatedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val reservation = Reservation(screening, selectedSeats)

        // when
        val price = reservation.calculatePrice()

        // then
        assertThat(price).isEqualTo(Money(30000))
    }
}
