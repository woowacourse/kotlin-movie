import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ScheduledScreenTest {
    @Test
    fun `영화, 상영관, 기간이 같은 일정은 동일한 일정으로 판단한다`() {
        assertThat(
            ScheduledScreen(
                movie = oneHourMovie,
                cinemaScreen = screenOne,
                screenTime = oneHourScreenTime,
            ),
        ).isEqualTo(
            ScheduledScreen(
                movie = oneHourMovie,
                cinemaScreen = screenOne,
                screenTime = oneHourScreenTime,
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("differentScheduledScreens")
    fun `영화, 상영관, 기간 중 하나만 다르더라도 다른 일정으로 판단한다`(
        movies: List<Movie>,
        cinemaScreens: List<CinemaScreen>,
        screenTimeGroup: List<DateTimeRange>,
    ) {
        assertThat(
            ScheduledScreen(
                movie = movies[0],
                cinemaScreen = cinemaScreens[0],
                screenTime = screenTimeGroup[0],
            ),
        ).isNotEqualTo(
            ScheduledScreen(
                movie = movies[1],
                cinemaScreen = cinemaScreens[1],
                screenTime = screenTimeGroup[1],
            ),
        )
    }

    @Test
    fun `상영관의 운영시간보다 영화 상영 일정이 일찍 배정되면 예외를 발생시킨다`() {
        assertThatThrownBy {
            ScheduledScreen(
                movie =
                    Movie(
                        id = onlyOneUuid,
                        runningTime = RunningTime(minute = 60),
                    ),
                cinemaScreen =
                    CinemaScreen(
                        id = onlyOneUuid,
                        servicePeriod =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 8, 0),
                                end = LocalDateTime.of(2026, 4, 8, 22, 0),
                            ),
                    ),
                screenTime =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 7, 0),
                        end = LocalDateTime.of(2026, 4, 8, 8, 0),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `상영관의 운영시간보다 영화 상영 일정이 늦게 배정되면 예외를 발생시킨다`() {
        assertThatThrownBy {
            ScheduledScreen(
                movie =
                    Movie(
                        id = onlyOneUuid,
                        runningTime = RunningTime(minute = 60),
                    ),
                cinemaScreen =
                    CinemaScreen(
                        id = onlyOneUuid,
                        servicePeriod =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 8, 0),
                                end = LocalDateTime.of(2026, 4, 8, 22, 0),
                            ),
                    ),
                screenTime =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 22, 0),
                        end = LocalDateTime.of(2026, 4, 8, 23, 0),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `상영관에 배정된 시간의 길이가 영화의 러닝타임과 일치하지 않으면 에외를 발생시킨다`() {
        assertThatThrownBy {
            ScheduledScreen(
                movie =
                    Movie(
                        id = onlyOneUuid,
                        runningTime = RunningTime(minute = 60),
                    ),
                cinemaScreen =
                    CinemaScreen(
                        id = onlyOneUuid,
                        servicePeriod =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 8, 0),
                                end = LocalDateTime.of(2026, 4, 8, 22, 0),
                            ),
                    ),
                screenTime =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 16, 0),
                        end = LocalDateTime.of(2026, 4, 8, 18, 0),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `상영관에 배정된 시간의 길이가 영화의 러닝타임과 일치하면 예외를 발생시키지 않는다`() {
        assertThatCode {
            ScheduledScreen(
                movie =
                    Movie(
                        id = onlyOneUuid,
                        runningTime = RunningTime(minute = 60),
                    ),
                cinemaScreen =
                    CinemaScreen(
                        id = onlyOneUuid,
                        servicePeriod =
                            DateTimeRange(
                                start = LocalDateTime.of(2026, 4, 8, 8, 0),
                                end = LocalDateTime.of(2026, 4, 8, 22, 0),
                            ),
                    ),
                screenTime =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 16, 0),
                        end = LocalDateTime.of(2026, 4, 8, 17, 0),
                    ),
            )
        }.doesNotThrowAnyException()
    }

    companion object {
        private val onlyOneUuid = Uuid.parse("11111111-1111-1111-1111-111111111111")
        private val onlyTwoUuid = Uuid.parse("22222222-2222-2222-2222-222222222222")
        private val oneHourMovie =
            Movie(
                id = onlyOneUuid,
                runningTime = RunningTime(minute = 60),
            )
        private val twoHourMovie =
            Movie(
                id = onlyTwoUuid,
                runningTime = RunningTime(minute = 120),
            )
        private val screenOne =
            CinemaScreen(
                id = onlyOneUuid,
                servicePeriod =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 8, 0),
                        end = LocalDateTime.of(2026, 4, 8, 22, 0),
                    ),
            )
        private val screenTwo =
            CinemaScreen(
                id = onlyTwoUuid,
                servicePeriod =
                    DateTimeRange(
                        start = LocalDateTime.of(2026, 4, 8, 7, 0),
                        end = LocalDateTime.of(2026, 4, 8, 22, 0),
                    ),
            )
        private val twoHourScreenTime =
            DateTimeRange(
                start = LocalDateTime.of(2026, 4, 8, 10, 0),
                end = LocalDateTime.of(2026, 4, 8, 12, 0),
            )
        private val oneHourScreenTime =
            DateTimeRange(
                start = LocalDateTime.of(2026, 4, 8, 11, 0),
                end = LocalDateTime.of(2026, 4, 8, 12, 0),
            )

        @JvmStatic
        fun differentScheduledScreens(): List<Arguments> =
            listOf(
                Arguments.of(
                    listOf(oneHourMovie, twoHourMovie),
                    listOf(screenOne, screenOne),
                    listOf(oneHourScreenTime, twoHourScreenTime),
                ),
                Arguments.of(
                    listOf(oneHourMovie, oneHourMovie),
                    listOf(screenOne, screenTwo),
                    listOf(oneHourScreenTime, oneHourScreenTime),
                ),
            )
    }
}
