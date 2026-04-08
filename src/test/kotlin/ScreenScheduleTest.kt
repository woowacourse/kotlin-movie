import model.DateTimeRange
import model.Movie
import model.MovieSchedule
import model.MovieScreening
import model.RunningTime
import model.ScreenSchedule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ScreenScheduleTest {
    @Test
    fun `운영 기간이 같더라도 id가 다르면 다른 상영관 일정으로 판단한다`() {
        val dateTimeRange =
            DateTimeRange(
                LocalDateTime.of(2026, 4, 7, 21, 50),
                LocalDateTime.of(2026, 4, 7, 22, 50),
            )
        assertThat(
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod = dateTimeRange,
                movieScreenings = emptyList(),
            ),
        ).isNotEqualTo(
            ScreenSchedule(
                screenId = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                servicePeriod = dateTimeRange,
                movieScreenings = emptyList(),
            ),
        )
    }

    @Test
    fun `운영 기간이 다르더라도 id가 같으면 같은 상영관 일정으로 판단한다`() {
        assertThat(
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        LocalDateTime.of(2026, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 7, 22, 50),
                    ),
                movieScreenings = emptyList(),
            ),
        ).isEqualTo(
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        LocalDateTime.of(1999, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 7, 22, 50),
                    ),
                movieScreenings = emptyList(),
            ),
        )
    }

    @Test
    fun `상영관의 운영시간보다 영화 상영 일정이 일찍 배정되면 예외를 발생시킨다`() {
        assertThatThrownBy {
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 7, 0),
                        end = LocalDateTime.of(2026, 4, 8, 8, 0),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                DateTimeRange(
                                    start = LocalDateTime.of(2026, 4, 8, 6, 0),
                                    end = LocalDateTime.of(2026, 4, 8, 7, 0),
                                ),
                        ),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `상영관의 운영시간보다 영화 상영 일정이 늦게 배정되면 예외를 발생시킨다`() {
        assertThatThrownBy {
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 7, 0),
                        end = LocalDateTime.of(2026, 4, 8, 8, 0),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                DateTimeRange(
                                    start = LocalDateTime.of(2026, 4, 8, 7, 30),
                                    end = LocalDateTime.of(2026, 4, 8, 8, 30),
                                ),
                        ),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `특정 영화를 요청받으면 해당하는 상영관의 영화 전체 일정을 반환한다`() {
        val movieOne =
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(10),
            )

        assertThat(
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        LocalDateTime.of(1999, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 10, 22, 50),
                    ),
                listOf(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                end = LocalDateTime.of(2026, 4, 8, 11, 10),
                            ),
                    ),
                ),
            ).getMovieSchedule(movieOne),
        ).isEqualTo(
            MovieSchedule(
                listOf(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                end = LocalDateTime.of(2026, 4, 8, 11, 10),
                            ),
                    ),
                ),
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("invalidMovieScreening")
    fun `동일한 상영관에서 상영 시간이 겹칠 경우 예외를 발생시킨다`(movieScreening: MovieScreening) {
        assertThatThrownBy {
            ScreenSchedule(
                screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                servicePeriod =
                    DateTimeRange(
                        LocalDateTime.of(1999, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 10, 22, 50),
                    ),
                listOf(
                    movieScreening,
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                end = LocalDateTime.of(2026, 4, 8, 12, 0),
                            ),
                    ),
                ),
            )
        }
    }

    companion object {
        private val movieOne =
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(60),
            )

        @JvmStatic
        fun invalidMovieScreening(): List<Arguments> =
            listOf(
                Arguments.of(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 10, 0),
                                end = LocalDateTime.of(2026, 4, 8, 11, 0),
                            ),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 12, 0),
                                end = LocalDateTime.of(2026, 4, 8, 13, 0),
                            ),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 11, 30),
                                end = LocalDateTime.of(2026, 4, 8, 12, 30),
                            ),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movieOne,
                        screenTime =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 10, 30),
                                end = LocalDateTime.of(2026, 4, 8, 11, 30),
                            ),
                    ),
                ),
            )
    }
}
