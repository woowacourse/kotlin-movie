package domain

import domain.movie.ShowingPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ShowingPeriodTest {
    @Test
    fun `시작일이 종료일보다 이후일 경우 예외가 발생한다`() {
        // given
        val startDate = LocalDate.of(2026, 4, 1)
        val endDate = startDate.minusYears(1)

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            ShowingPeriod(
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    @Test
    fun `특정 날짜가 상영 가능 기간에 포함되면 true를 반환한다`() {
        // given
        val showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30)
        )

        // when
        val actual = showingPeriod.isDateInRange(LocalDate.of(2026, 4, 10))

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `특정 날짜가 상영 가능 기간에 포함되지 않으면 false를 반환한다`() {
        // given
        val showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30)
        )

        // when
        val actual = showingPeriod.isDateInRange(LocalDate.of(2026, 5, 1))

        // then
        assertThat(actual).isFalse()
    }
}
