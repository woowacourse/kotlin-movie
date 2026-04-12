package movie

import movie.controller.MovieController
import movie.domain.Movie
import movie.domain.MovieManager
import movie.domain.Schedule
import movie.domain.Schedules
import java.time.LocalDateTime

fun main() {
    val schedule1 = Schedule(
        movie = Movie(
            title = "시동",
            runningTime = 120,
        ),
        startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
        endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
    )
    val schedule2 = Schedule(
        movie = Movie(
            title = "시동",
            runningTime = 120,
        ),
        startTime = LocalDateTime.of(2026, 4, 10, 13, 0),
        endTime = LocalDateTime.of(2026, 4, 10, 15, 0),
    )

    val schedule3 = Schedule(
        movie = Movie(
            title = "토토로",
            runningTime = 120,
        ),
        startTime = LocalDateTime.of(2026, 4, 11, 14, 0),
        endTime = LocalDateTime.of(2026, 4, 11, 19, 0),
    )

    val schedules = Schedules(listOf(schedule1, schedule2, schedule3))
    val movieManager = MovieManager(schedules)

    MovieController(movieManager).run()
}
