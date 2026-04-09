package movie.domain.screening

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreeningDateTimeTest {
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

    @Test
    fun `매달 10일, 20일, 30일은 무비데이로 판단한다`() {
        val screeningDateTime1 = ScreeningDateTime(LocalDate.of(2026, 1, 10), LocalTime.of(10, 0), LocalTime.of(12, 0))

        val result = screeningDateTime1.isMovieDay()

        assertThat(result).isTrue()
    }

    @Test
    fun `11시 이전 상영 혹은 20시 이후 상영 시작은 시간할인 대상으로 판단한다`() {
        val screeningDateTime1 = ScreeningDateTime(LocalDate.of(2026, 1, 11), LocalTime.of(10, 0), LocalTime.of(12, 0))
        val screeningDateTime2 = ScreeningDateTime(LocalDate.of(2026, 1, 11), LocalTime.of(21, 0), LocalTime.of(22, 30))

        val result1 = screeningDateTime1.isTimeDiscountTarget()
        val result2 = screeningDateTime2.isTimeDiscountTarget()

        assertThat(result1).isTrue()
        assertThat(result2).isTrue()
    }

    @Test
    fun `그 외 시간은 시간할인 대상이 아닌 것으로 판단한다`() {
        val screeningDateTime1 = ScreeningDateTime(LocalDate.of(2026, 1, 11), LocalTime.of(11, 0), LocalTime.of(12, 0))

        val result = screeningDateTime1.isTimeDiscountTarget()

        assertThat(result).isFalse()
    }
}
