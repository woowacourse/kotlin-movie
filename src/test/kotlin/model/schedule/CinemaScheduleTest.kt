package model.schedule

import model.CinemaTime
import model.CinemaTimeRange
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CinemaScheduleTest {
    private val uuidOne = "1"
    private val uuidTwo = "2"

    @Test
    fun `동일한 ScreenSchedule이 들어오면 예외를 반환한다`() {
        Assertions
            .assertThatThrownBy {
                val cinemaTimeRange =
                    CinemaTimeRange(
                        CinemaTime(LocalDateTime.of(2026, 4, 7, 21, 50)),
                        CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 50)),
                    )

                val schedules =
                    listOf(
                        ScreenSchedule(
                            screenId = uuidOne,
                            servicePeriod = cinemaTimeRange,
                            movieScreenings = emptyList(),
                        ),
                        ScreenSchedule(
                            screenId = uuidOne,
                            servicePeriod = cinemaTimeRange,
                            movieScreenings = emptyList(),
                        ),
                    )
                CinemaSchedule(
                    screenSchedules = schedules,
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
