package movie.domain.screening

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ScreeningDateTimeTest {
    @Test
    fun `시작 시간이 끝 시간보다 이전이 아니면 예외를 반환한다`() {
        // given
        val date = LocalDate.of(2026, 1, 1)
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(9, 0)

        // then
        assertThrows<IllegalArgumentException> {
            ScreeningDateTime(date, startTime, endTime)
        }
    }

    @Test
    fun `시작 시간은 끝 시간보다 이전이어야 한다`() {
        // given
        val date = LocalDate.of(2026, 1, 1)
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(11, 0)

        // when
        val screeningDateTime = ScreeningDateTime(date, startTime, endTime)

        // then
        assertThat(screeningDateTime.startTime).isBefore(screeningDateTime.endTime)
    }

    @Test
    fun `시간이 겹치는 상영일시는 겹침으로 판단한다`() {
        // given
        val screeningDateTime1 = ScreeningDateTime(LocalDate.of(2026, 1, 1), LocalTime.of(10, 0), LocalTime.of(12, 0))
        val screeningDateTime2 = ScreeningDateTime(LocalDate.of(2026, 1, 1), LocalTime.of(11, 0), LocalTime.of(13, 0))

        // when
        val result = screeningDateTime1.isOverlapping(screeningDateTime2)
        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `시간이 겹치지 않는 상영일시는 겹치지 않음으로 판단한다`() {
        val screeningDateTime1 = ScreeningDateTime(LocalDate.of(2026, 1, 1), LocalTime.of(10, 0), LocalTime.of(12, 0))
        val screeningDateTime2 = ScreeningDateTime(LocalDate.of(2026, 1, 1), LocalTime.of(13, 0), LocalTime.of(15, 0))

        val result = screeningDateTime1.isOverlapping(screeningDateTime2)
        assertThat(result).isFalse()
    }
}
