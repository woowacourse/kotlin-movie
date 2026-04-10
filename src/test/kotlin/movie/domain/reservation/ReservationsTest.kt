package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.movie.Movie
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
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `예매를 추가할 수 있다`() {
        // given
        val reservationData = ReservationData.reservations
        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(13, 0),
                    LocalTime.of(14, 0),
                ),
                ReservedSeats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))
        val addedReservation = Reservation(movie, screening, selectedSeats)
        val reservations = Reservations(reservationData)

        // when
        val result = reservations.add(addedReservation)

        // then
        assertThat(result).isEqualTo(reservationData + addedReservation)
    }

    @Test
    fun `시간이 겹치는 예매를 추가할 수 없다`() {
        val reservationData = ReservationData.reservations
        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(13, 0),
                ),
                ReservedSeats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))
        val addedReservation = Reservation(movie, screening, selectedSeats)
        val reservations = Reservations(reservationData)

        val exception =
            assertThrows<IllegalArgumentException> {
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
