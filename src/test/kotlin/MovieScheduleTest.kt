import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieScheduleTest {
    @Test
    fun `특정 영화의 상영 일정 목록에 포함된 모든 영화들이 같은 영화가 아니라면 예외를 발생시킨다`() {
        val movieOne =
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(10),
            )
        val movieTwo =
            Movie(
                id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                runningTime = RunningTime(100),
            )

        val cinemaScreen =
            CinemaScreen(
                id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                servicePeriod =
                    DateTimeRange(
                        LocalDateTime.of(1999, 4, 7, 21, 50),
                        LocalDateTime.of(2026, 4, 7, 22, 50),
                    ),
            )

        assertThatThrownBy {
            MovieSchedule(
                ScheduledScreen(
                    movie = movieOne,
                    cinemaScreen = cinemaScreen,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 11, 0),
                            end = LocalDateTime.of(2026, 4, 8, 11, 10),
                        ),
                ),
                ScheduledScreen(
                    movie = movieOne,
                    cinemaScreen = cinemaScreen,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 11, 20),
                            end = LocalDateTime.of(2026, 4, 8, 11, 30),
                        ),
                ),
                ScheduledScreen(
                    movie = movieTwo,
                    cinemaScreen = cinemaScreen,
                    screenTime =
                        DateTimeRange(
                            start = LocalDateTime.of(2026, 4, 8, 11, 40),
                            end = LocalDateTime.of(2026, 4, 8, 13, 20),
                        ),
                ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
