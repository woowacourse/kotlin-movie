package model

import model.movie.Movie
import model.movie.Movies
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MoviesTest {
    private val movie1 =
        Movie(
            title = "스파이더맨",
            runningTime = RunningTime(120L),
        )

    @Test
    fun `영화 목록이 비어있으면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Movies(emptyList())
        }
    }

    @Test
    fun `영화목록에 찾는 영화가 있으면 true를 반환한다`() {
        val movies = Movies(listOf(movie1))

        assertThat(movies.isInclude(movie1)).isTrue()
    }

    @Test
    fun `영화목록에 찾는 영화가 없으면 false를 반환한다`() {
        val movie2 =
            Movie(
                title = "스파이더맨2",
                runningTime = RunningTime(120L),
            )
        val movies = Movies(listOf(movie1))

        assertThat(movies.isInclude(movie2)).isFalse()
    }

    @Test
    fun `제목으로 영화를 찾을 수 있다`() {
        val movies = Movies(listOf(movie1))

        assertThat(movies.findByTitle("스파이더맨")).isEqualTo(movie1)
    }

    @Test
    fun `존재하지 않는 제목으로 찾으면 null을 반환한다`() {
        val movies = Movies(listOf(movie1))

        assertThat(movies.findByTitle("없는 영화")).isNull()
    }
}
