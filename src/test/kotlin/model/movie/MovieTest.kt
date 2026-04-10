package model.movie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class MovieTest {
    @Test
    fun `영화는 제목과 상영시간을 가지고 있다`() {
        val movie = createMovie()

        assertThat(movie.movieTitle).isEqualTo("인터스텔라")
        assertThat(movie.movieRunningTime).isEqualTo(170L)
    }

    @Test
    fun `영화의 상영시간은 0보다 커야 한다`() {
        assertThrows<IllegalArgumentException> {
            createMovie(movieRunningTime = 0)
        }
    }

    @Test
    fun `영화의 시작 상영 가능일은 종료 상영 가능일보다 클 수 없다`() {
        assertThrows<IllegalArgumentException> {
            createMovie(
                startDate = LocalDate.of(2026, 4, 30),
                endDate = LocalDate.of(2026, 4, 1),
            )
        }
    }

    private fun createMovie(
        movieTitle: String = "인터스텔라",
        movieRunningTime: Long = 170,
        startDate: LocalDate = LocalDate.of(2026, 4, 1),
        endDate: LocalDate = LocalDate.of(2026, 4, 30),
    ): Movie = Movie(movieTitle, movieRunningTime, startDate, endDate)
}
