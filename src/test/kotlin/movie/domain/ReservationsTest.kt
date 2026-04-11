package movie.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ReservationsTest {
    @Test
    fun `스케줄이 정상적으로 추가된다`() {
        val reservations = Reservations()

        assertDoesNotThrow {
            reservations.addReservation(reservation)
        }
    }

    @Test
    fun `시간이 겹치는 영화를 추가한다면 예외가 발생한다`() {
        val reservations = Reservations()

        reservations.addReservation(reservation)

        assertThrows<IllegalArgumentException> {
            reservations.addReservation(reservation)
        }
    }

    @Test
    fun `영화 시간이 겹친다면 true를 반환한다`() {
        val reservations = Reservations()

        reservations.addReservation(reservation)

        assertThat(reservations.isDuplicateTime(dupReservation)).isTrue
    }

    @Test
    fun `영화 시간이 겹치지 않는다면 false 반환한다`() {
        val reservations = Reservations()

        reservations.addReservation(reservation)

        assertThat(reservations.isDuplicateTime(schedule2)).isFalse
    }

    companion object {
        val movie = Movie("아이언맨", 200)
        val reservation = Reservation(
            schedule = Schedule(
                movie = movie,
                startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                endTime = LocalDateTime.of(2026, 4, 10, 12, 40)
            ),
            seats = emptyList()
        )

        val dupReservation = Reservation(
            schedule = Schedule(
                movie = movie,
                startTime = LocalDateTime.of(2026, 4, 10, 12, 0),
                endTime = LocalDateTime.of(2026, 4, 10, 14, 40)
            ),
            seats = emptyList()
        )
        val schedule2 = Reservation(
            schedule = Schedule(
                movie = movie,
                startTime = LocalDateTime.of(2026, 4, 10, 12, 50),
                endTime = LocalDateTime.of(2026, 4, 10, 14, 50)
            ),
            seats = emptyList()
        )
    }
}
