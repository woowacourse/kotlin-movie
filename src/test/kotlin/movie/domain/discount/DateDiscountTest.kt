package movie.domain.discount

import movie.domain.Price
import movie.fixture.ScheduleFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DateDiscountTest {
    @Test
    fun `무비 데이일 경우 할인이 적용된다`() {
        val movieDay = MovieDayPolicy()
        val schedule = ScheduleFixture.createSchedule(
            startTime = LocalDateTime.of(2026, 4, 10, 0, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 1, 0)
        )

        val result = movieDay.discount(Price(10_000), schedule)

        assertThat(result).isEqualTo(Price(9_000))
    }

    @Test
    fun `무비 데이가 아닐 경우 할인이 적용되지 않는다`() {
        val movieDay = MovieDayPolicy()
        val schedule = ScheduleFixture.createSchedule(
            startTime = LocalDateTime.of(2026, 4, 11, 0, 0),
            endTime = LocalDateTime.of(2026, 4, 11, 1, 0)
        )

        val result = movieDay.discount(Price(10_000), schedule)

        assertThat(result).isEqualTo(Price(10_000))
    }

    @Test
    fun `할인 시간일 경우 할인이 적용된다`() {
        val time = TimePolicy()
        val schedule = ScheduleFixture.createSchedule(
            startTime = LocalDateTime.of(2026, 4, 10, 0, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 1, 0)
        )

        val result = time.discount(Price(10_000), schedule)

        assertThat(result).isEqualTo(Price(8_000))
    }

    @Test
    fun `할인 시간이 아닐 경우 할인이 적용되지 않는다`() {
        val time = TimePolicy()
        val schedule = ScheduleFixture.createSchedule(
            startTime = LocalDateTime.of(2026, 4, 11, 13, 0),
            endTime = LocalDateTime.of(2026, 4, 11, 14, 0)
        )

        val result = time.discount(Price(10_000), schedule)

        assertThat(result).isEqualTo(Price(10_000))
    }
}
