package movie.domain.user

import movie.domain.amount.Point
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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class UserTest {
    @Test
    fun `예매 목록과 포인트를 갖고 있다`() {
        // given
        val reservations = Reservations(listOf(reservationFixture()))
        val point = Point(1000)

        // when
        val user = User(reservations, point)

        // then
        assertThat(user.reservations).isEqualTo(reservations)
        assertThat(user.point).isEqualTo(point)
    }

    @Test
    fun `보유 포인트를 초과하면 사용할 수 없다`() {
        // given
        val user = User(Reservations(emptyList()), Point(1000))

        // when & then
        assertThatThrownBy { user.usablePoint(1500) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("보유 포인트를 초과할 수 없습니다.")
    }

    private fun reservationFixture(): Reservation {
        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
                ),
            )
        val screening =
            Screening(
                id = 101L,
                movie = MovieTitle("토이 스토리"),
                slot =
                    ScreeningSlot(
                        screen = Screen(ScreenId(1), Seats.createDefault()),
                        screeningDateTime =
                            ScreeningDateTime(
                                LocalDate.of(2026, 1, 1),
                                LocalTime.of(10, 0),
                                LocalTime.of(12, 0),
                            ),
                    ),
                reservatedSeats = ReservatedSeats(emptyList()),
            )
        return Reservation(screening, selectedSeats)
    }
}
