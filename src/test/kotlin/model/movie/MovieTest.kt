package model.movie

import model.fixture.MovieFixture
import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieTest {
    @Test
    fun `id가 같으면 같은 영화로 판단한다`() {
        val movieId = MovieId(Uuid.generateV7())
        val movie1 = MovieFixture.create(id = movieId)
        val movie2 = MovieFixture.create(id = movieId)
        assertThat(
            movie1,
        ).isEqualTo(
            movie2,
        )
    }

    @Test
    fun `러닝 타임이 같아도 id가 다르면 다른 영화로 판단한다`() {
        val movie1 = MovieFixture.create(runningTime = RunningTime(30))
        val movie2 = MovieFixture.create(runningTime = RunningTime(30))
        assertThat(
            movie1,
        ).isNotEqualTo(
            movie2,
        )
    }

    @Test
    fun `영화의 이름이 같아도 id가 다르면 다른 영화로 판단한다`() {
        val movie1 = MovieFixture.create(name = MovieName("영화1"))
        val movie2 = MovieFixture.create(name = MovieName("영화1"))
        assertThat(
            movie1,
        ).isNotEqualTo(
            movie2,
        )
    }
}
