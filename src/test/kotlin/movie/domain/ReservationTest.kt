package movie.domain

import movie.domain.seat.Seat
import movie.domain.seat.SeatNumber
import movie.domain.seat.SeatRank
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ReservationTest {
    @Test
    fun `스케줄과 선택한 좌석들을 리스트로 전달하면 예약 객체가 생성된다`() {
        val seats = listOf(
            Seat(SeatNumber(row = 'A', col = 1), SeatRank.S),
            Seat(SeatNumber(row = 'A', col = 2), SeatRank.S)
        )

        val reservation = Reservation(schedule = schedule, seats = seats)

        assertThat(reservation.schedule).isEqualTo(schedule)
        assertThat(reservation.seats).isEqualTo(seats)
    }

    @Test
    fun `예약된 모든 좌석의 가격을 합산하여 총 금액을 반환한다`() {
        val seats = listOf(
            Seat(SeatNumber(row = 'A', col = 1), SeatRank.S),
            Seat(SeatNumber(row = 'A', col = 1), SeatRank.A),
            Seat(SeatNumber(row = 'A', col = 1), SeatRank.B)
        )
        val reservation = Reservation(schedule = schedule, seats = seats)

        val totalPrice = reservation.calculateTotalPrice()

        assertThat(totalPrice).isEqualTo(Price(45_000))
    }

    companion object {
        private val movie = Movie("아이언맨", 200)
        private val schedule = Schedule(
            movie = movie,
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 40)
        )
    }
}
