package movie.domain.screening

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ScreeningDateTimeTest {
    @Test
    fun `시작 시간이 끝 시간보다 이전이 아니면 예외를 반환한다`() {
        // given
        val startAt = LocalDateTime.of(2026, 1, 1, 10, 0)
        val endAt = LocalDateTime.of(2026, 1, 1, 9, 0)

        // then
        assertThrows<IllegalArgumentException> {
            ScreeningDateTime(startAt, endAt)
        }
    }

    @Test
    fun `시작 시간은 끝 시간보다 이전이어야 한다`() {
        // given
        val startAt = LocalDateTime.of(2026, 1, 1, 10, 0)
        val endAt = LocalDateTime.of(2026, 1, 1, 11, 0)

        // when
        val screeningDateTime = ScreeningDateTime(startAt, endAt)

        // then
        assertThat(screeningDateTime.startAt).isBefore(screeningDateTime.endAt)
    }

    @Test
    fun `시간이 겹치는 상영일시는 겹침으로 판단한다`() {
        // given
        val screeningDateTime1 =
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0),
            )
        val screeningDateTime2 =
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 11, 0),
                LocalDateTime.of(2026, 1, 1, 13, 0),
            )

        // when
        val result = screeningDateTime1.isOverlapping(screeningDateTime2)
        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `시간이 겹치지 않는 상영일시는 겹치지 않음으로 판단한다`() {
        val screeningDateTime1 =
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0),
            )
        val screeningDateTime2 =
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 13, 0),
                LocalDateTime.of(2026, 1, 1, 15, 0),
            )

        val result = screeningDateTime1.isOverlapping(screeningDateTime2)
        assertThat(result).isFalse()
    }
}
