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
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `예매를 추가할 수 있다`() {
        // given
        val reservationData = ReservationData.reservations
        val selectedSeats = SelectedSeats(listOf(
            Seat("A", 1, SeatGrade.B),
            Seat("C", 1, SeatGrade.S)
        ))

        val screening = Screening(
            1,
            Screen(1, Seats.createDefault()),
            ScreeningDateTime(
                LocalDate.of(2026, 1, 1),
                LocalTime.of(13,0),
                LocalTime.of(14,0)),
            ReservatedSeats(listOf(
                Seat("C", 1, SeatGrade.S),
                Seat("C", 2, SeatGrade.S),
                Seat("E", 1, SeatGrade.A),
            )),

            )
        val addedReservation = Reservation(screening, selectedSeats)
        val reservations = Reservations(reservationData)

        // when
        val result = reservations.add(addedReservation)

        // then
        assertThat(result).isEqualTo(reservationData + addedReservation)
    }

    @Test
    fun `시간이 겹치는 예매를 추가할 수 없다`() {
        val reservationData = ReservationData.reservations
        val selectedSeats = SelectedSeats(listOf(
            Seat("A", 1, SeatGrade.B),
            Seat("C", 1, SeatGrade.S)
        ))

        val screening = Screening(
            1,
            Screen(1, Seats.createDefault()),
            ScreeningDateTime(
                LocalDate.of(2026, 1, 1),
                LocalTime.of(10,0),
                LocalTime.of(13,0)),
            ReservatedSeats(listOf(
                Seat("C", 1, SeatGrade.S),
                Seat("C", 2, SeatGrade.S),
                Seat("E", 1, SeatGrade.A),
            )),

            )
        val addedReservation = Reservation(screening, selectedSeats)
        val reservations = Reservations(reservationData)

        val exception = assertThrows<IllegalArgumentException> {
            reservations.add(addedReservation)
        }
        assert(exception.message == "상영 시간이 겹치는 예매가 존재합니다.")
    }

    @Test
    fun `전체 원가는 각 예매 원가의 합이다`() {
        // given
        val reservationData = ReservationData.reservations
        val reservations = Reservations(reservationData)

        // then
        assert(reservations.totalPrice() == Money(60000))
    }
}
