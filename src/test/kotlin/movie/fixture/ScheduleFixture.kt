package movie.fixture

import movie.domain.Movie
import movie.domain.MovieTitle
import movie.domain.Schedule
import java.time.LocalDateTime

object ScheduleFixture {
    fun createSchedule(
        title: String = "시동",
        runningTime: Int = 120,
        startTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
        endTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 12, 0),
    ) = Schedule(
        id = 1L,
        movie = Movie(title = MovieTitle(title), runningTime = runningTime),
        startTime = startTime,
        endTime = endTime,
    )
}
