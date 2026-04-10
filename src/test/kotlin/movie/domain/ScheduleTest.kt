package movie.domain

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
}
