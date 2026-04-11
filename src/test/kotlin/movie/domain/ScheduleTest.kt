package movie.domain

import movie.domain.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ScheduleTest {
    @Test
    fun `영화 시작 시각이 종료 시각보다 느릴경우 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            Schedule(
                movie = Movie(
                    title = "시동",
                    runningTime = 120,
                ),
                startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                endTime = LocalDateTime.of(2026, 4, 10, 0, 0),
            )
        }
    }

    @Test
    fun `영화 시간이 겹치지 않는다면 False를 반환한다`() {
        val schedule1 = Schedule(
            movie = Movie(
                title = "시동",
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
        )
        val schedule2 = Schedule(
            movie = Movie(
                title = "시동",
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 12, 30),
            endTime = LocalDateTime.of(2026, 4, 10, 13, 0),
        )

        assertThat(schedule1.isDuplicateTime(schedule2)).isFalse
    }

    @Test
    fun `영화 시간이 겹친다면 True를 반환한다`() {
        val schedule1 = Schedule(
            movie = Movie(
                title = "시동",
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
        )
        val schedule2 = Schedule(
            movie = Movie(
                title = "시동",
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 11, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 13, 0),
        )

        assertThat(schedule1.isDuplicateTime(schedule2)).isTrue
    }

    @Test
    fun `유효하지 않은 좌석을 예매하려고 하면 예외가 발생한다`() {
        val schedule = Schedule(
            movie = Movie(
                title = "시동",
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
        )

        val seatNumbers = listOf(
            SeatNumber(row = 'A', col = 1),
            SeatNumber(row = 'Z', col = 1),
        )

        assertThrows<IllegalArgumentException> {
            schedule.addSeats(seatNumbers)
        }
    }
}
