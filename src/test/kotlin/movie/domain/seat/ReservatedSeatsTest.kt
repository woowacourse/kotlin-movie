package movie.domain.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReservatedSeatsTest {
    private val seats =
        listOf(
            Seat("C", 1, SeatGrade.S),
            Seat("C", 2, SeatGrade.S),
            Seat("E", 1, SeatGrade.A),
        )

    @Test
    fun `예약되지 않은 좌석은 예약 가능으로 판단한다`() {
        // given
        val reservatedSeats = ReservatedSeats(seats)

        // when
        val result = reservatedSeats.isAvailable(Seat("A", 1, SeatGrade.S))

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `이미 예약된 좌석은 예약 불가로 판단한다`() {
        // given
        val reservatedSeats = ReservatedSeats(seats)

        // when
        val result = reservatedSeats.isAvailable(Seat("C", 1, SeatGrade.S))

        // then
        assertThat(result).isFalse()
    }

    @Test
    fun `좌석을 추가하면 해당 좌석이 예약이 불가하다`() {
        // given
        val reservatedSeats = ReservatedSeats(seats)

        // when
        val newReservatedSeats = reservatedSeats.add(listOf(Seat("A", 1, SeatGrade.B)))
        val result = ReservatedSeats(newReservatedSeats.getSeats()).isAvailable(Seat("A", 1, SeatGrade.B))

        // then
        assertThat(result).isFalse()
    }
}
