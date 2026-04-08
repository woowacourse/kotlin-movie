package schedule

import model.DateTimeRange
import model.movie.Movie
import model.movie.RunningTime
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import model.seat.SeatGroup
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MovieScheduleTest {
    @Test
    fun `특정 영화의 상영 일정 목록에 포함된 모든 영화들이 같은 영화가 아니라면 예외를 발생시킨다`() {
        val movieOne =
            Movie(
                id = "1",
                runningTime = RunningTime(10),
            )
        val movieTwo =
            Movie(
                id = "2",
                runningTime = RunningTime(100),
            )

        Assertions
            .assertThatThrownBy {
                MovieSchedule(
                    listOf(
                        MovieScreening(
                            movie = movieOne,
                            screenTime =
                                DateTimeRange(
                                    start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                    end = LocalDateTime.of(2026, 4, 8, 11, 10),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                        MovieScreening(
                            movie = movieOne,
                            screenTime =
                                DateTimeRange(
                                    start = LocalDateTime.of(2026, 4, 8, 11, 20),
                                    end = LocalDateTime.of(2026, 4, 8, 11, 30),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                        MovieScreening(
                            movie = movieTwo,
                            screenTime =
                                DateTimeRange(
                                    start = LocalDateTime.of(2026, 4, 8, 11, 40),
                                    end = LocalDateTime.of(2026, 4, 8, 13, 20),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                    ),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
