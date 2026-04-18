package movie.fixture

import movie.domain.Movie
import movie.domain.MovieTitle
import movie.domain.Schedule
import movie.domain.Schedules
import java.time.LocalDateTime

object SchedulesFixture {
    val defaultSchedules =
        Schedules(
            listOf(
                Schedule(
                    id = 1L,
                    movie = Movie(title = MovieTitle("시동"), runningTime = 120),
                    startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                    endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
                ),
                Schedule(
                    id = 1L,
                    movie = Movie(title = MovieTitle("시동"), runningTime = 120),
                    startTime = LocalDateTime.of(2026, 4, 10, 13, 0),
                    endTime = LocalDateTime.of(2026, 4, 10, 15, 0),
                ),
                Schedule(
                    id = 1L,
                    movie = Movie(title = MovieTitle("토토로"), runningTime = 120),
                    startTime = LocalDateTime.of(2026, 4, 11, 14, 0),
                    endTime = LocalDateTime.of(2026, 4, 11, 19, 0),
                ),
            ),
        )
}
