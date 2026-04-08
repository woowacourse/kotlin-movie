import model.CinemaSchedule
import model.DateTimeRange
import model.Movie
import model.MovieSchedule
import model.MovieScreening
import model.RunningTime
import model.ScreenSchedule
import model.SeatGroup
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CinemaScheduleTest {
    @Test
    fun `모든 상영관의 영화 상영 일정을 반환한다`() {
        val uuidOne = Uuid.parse("11111111-1111-1111-1111-111111111111")
        val uuidTwo = Uuid.parse("22222222-2222-2222-2222-222222222222")

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

        assertThat(
            CinemaSchedule(
                screenSchedules = schedules,
            ).getMovieSchedule(movie),
        ).isEqualTo(expected)
    }

    @Test
    fun `동일한 ScreenSchedule이 들어오면 예외를 반환한다`() {
        assertThatThrownBy {
            val uuidOne = Uuid.parse("11111111-1111-1111-1111-111111111111")
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
