package movie.domain

import movie.domain.MovieTitle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class MovieManagerTest {
    @Test
    fun `특정 영화의 시작 시간을 반환한다`() {
        val movieManager = MovieManager(
            schedules = Schedules(listOf(schedule1, schedule2, schedule3))
        )

        val startTimes = movieManager.getMovieStartTime(
            movieTitle = MovieTitle("시동"),
            date = LocalDate.of(2026, 4, 10),
        )

        assertThat(startTimes).isEqualTo(
            listOf(
                LocalDateTime.of(2026, 4, 10, 10, 0),
                LocalDateTime.of(2026, 4, 10, 11, 0),
            )
        )
    }

    companion object {
        val schedule1 = Schedule(
            movie = Movie(
                title = MovieTitle("시동"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
        )
        val schedule2 = Schedule(
            movie = Movie(
                title = MovieTitle("시동"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 11, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 13, 0),
        )

        val schedule3 = Schedule(
            movie = Movie(
                title = MovieTitle("토토로"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 14, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 19, 0),
        )
    }
}
