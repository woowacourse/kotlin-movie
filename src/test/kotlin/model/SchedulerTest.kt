package model

import model.movie.Movie
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SchedulerTest {
    private val scheduler = Scheduler()

    @Test
    fun `해당 영화의 Screen이 없으면 빈 Screenings를 반환한다`() {
        val unknownMovie =
            Movie(
                title = "없는 영화",
                runningTime = RunningTime(120),
            )

        val screenings = scheduler.getScreenings(unknownMovie, LocalDate.of(2025, 9, 20))

        assertThat(screenings.isEmpty()).isTrue()
    }

    @Test
    fun `영화와 날짜에 맞는 Screenings를 반환한다`() {
        val movies = scheduler.getMovies()
        val f1Movie = movies.findByTitle("F1 더 무비")!!

        val screenings = scheduler.getScreenings(f1Movie, LocalDate.of(2025, 9, 20))

        assertThat(screenings).hasSize(4)
    }

    @Test
    fun `전체 영화 목록을 반환한다`() {
        val movies = scheduler.getMovies()

        assertThat(movies.isInclude(movies.findByTitle("F1 더 무비")!!)).isTrue()
    }
}
