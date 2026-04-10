package movie

import model.movie.Movie
import model.movie.MovieCatalog
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieCatalogTest {
    @Test
    fun `영화 목록에 포함된 영화 제목을 입력하면 영화를 반환한다`() {
        val id1 = MovieId(Uuid.generateV7())
        val id2 = MovieId(Uuid.generateV7())
        val movie1 =
            Movie(
                id = id1,
                name = MovieName("영화1"),
                runningTime = RunningTime(100),
            )

        val movie2 =
            Movie(
                id = id2,
                name = MovieName("영화2"),
                runningTime = RunningTime(30),
            )

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
        val id1 = MovieId(Uuid.generateV7())
        val id2 = MovieId(Uuid.generateV7())
        val movie1 =
            Movie(
                id = id1,
                name = MovieName("영화1"),
                runningTime = RunningTime(100),
            )

        val movie2 =
            Movie(
                id = id2,
                name = MovieName("영화2"),
                runningTime = RunningTime(30),
            )

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
