package schedule

import model.DateTimeRange
import model.movie.Movie
import model.movie.RunningTime
import model.schedule.CinemaSchedule
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import model.schedule.ScreenSchedule
import model.seat.SeatGroup
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CinemaScheduleTest {
    private val uuidOne = "1"
    private val uuidTwo = "2"

    @Test
    fun `모든 상영관의 영화 상영 일정을 반환한다`() {
        val movie =
            Movie(
                id = uuidOne,
                runningTime = RunningTime(minute = 60),
            )

        val dateTimeRange =
            DateTimeRange(
                LocalDateTime.of(2026, 4, 7, 21, 50),
                LocalDateTime.of(2026, 4, 10, 22, 50),
            )

        val movieScreening =
            listOf(
                MovieScreening(
                    movie = movie,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 16, 0),
                            end = LocalDateTime.of(2026, 4, 8, 17, 0),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
            )

        val expectedMovieScreening =
            listOf(
                MovieScreening(
                    movie = movie,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 16, 0),
                            end = LocalDateTime.of(2026, 4, 8, 17, 0),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
                MovieScreening(
                    movie = movie,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 16, 0),
                            end = LocalDateTime.of(2026, 4, 8, 17, 0),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
            )

        val schedules =
            listOf(
                ScreenSchedule(
                    screenId = uuidOne,
                    servicePeriod = dateTimeRange,
                    movieScreenings = movieScreening,
                ),
                ScreenSchedule(
                    screenId = uuidTwo,
                    servicePeriod = dateTimeRange,
                    movieScreenings = movieScreening,
                ),
            )

        val expected = MovieSchedule(movieScreenings = expectedMovieScreening)

        Assertions
            .assertThat(
                CinemaSchedule(
                    screenSchedules = schedules,
                ).getMovieSchedule(movie),
            ).isEqualTo(expected)
    }

    @Test
    fun `동일한 ScreenSchedule이 들어오면 예외를 반환한다`() {
        Assertions
            .assertThatThrownBy {
                val dateTimeRange =
                    DateTimeRange(
                        LocalDateTime.of(2026, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 10, 22, 50),
                    )

                val schedules =
                    listOf(
                        ScreenSchedule(
                            screenId = uuidOne,
                            servicePeriod = dateTimeRange,
                            movieScreenings = emptyList(),
                        ),
                        ScreenSchedule(
                            screenId = uuidOne,
                            servicePeriod = dateTimeRange,
                            movieScreenings = emptyList(),
                        ),
                    )
                CinemaSchedule(
                    screenSchedules = schedules,
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
