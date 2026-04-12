package model.movie

import model.fixture.MovieFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MovieCatalogTest {
    private val movie1 = MovieFixture.create(name = MovieName("영화1"))
    private val movie2 = MovieFixture.create(name = MovieName("영화2"))

    @Test
    fun `영화 목록에 포함된 영화 제목을 입력하면 영화를 반환한다`() {
        val movieCatalog =
            MovieCatalog(
                movies =
                    listOf(
                        movie1,
                        movie2,
                    ),
            )
        assertThat(movieCatalog.findByName("영화1")).isEqualTo(movie1)
    }

    @Test
    fun `영화 목록에 포함되어 있지 않은 영화 제목을 입력하면 null을 반환한다`() {
        val movieCatalog =
            MovieCatalog(
                movies =
                    listOf(
                        movie1,
                        movie2,
                    ),
            )
        assertThat(movieCatalog.findByName("영화3")).isNull()
    }
}
