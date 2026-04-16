@file:Suppress("NonAsciiCharacters")

package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.database.DatabaseInitializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JdbcMovieRepositoryTest {
    private val isLocal = false
    private val movieRepository = JdbcMovieRepository(isLocal)

    private fun dropTables() {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            conn.createStatement().use { statement ->
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE")
                statement.execute("DROP TABLE IF EXISTS movie")
                statement.execute("DROP TABLE IF EXISTS screening")
                statement.execute("DROP TABLE IF EXISTS reservation")
                statement.execute("DROP TABLE IF EXISTS reserved_seat")
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE")
            }
        }
    }

    @BeforeEach
    fun setUp() {
        dropTables()
        DatabaseInitializer.initTables(isLocal)
    }

    @Test
    fun `데이터베이스에 저장된 영화 정보를 정확히 조회하여 Movie 도메인 객체로 매핑한다`() {
        // given & when
        val movies = movieRepository.getMovies().toList()

        // then
        assertThat(movies).hasSize(4)
        assertThat(movies[0].title).isEqualTo("F1 더 무비")
        assertThat(movies[1].title).isEqualTo("토이 스토리")
        assertThat(movies[2].title).isEqualTo("아이언맨")
        assertThat(movies[3].title).isEqualTo("로봇 드림")
    }

    @Test
    fun `제목으로 영화 정보를 조회한다`() {
        // given & when
        val movie = movieRepository.findByTitle("아이언맨")

        // then
        assertThat(movie).isNotNull
        assertThat(movie?.title).isEqualTo("아이언맨")
    }

    @Test
    fun `ID로 영화 정보를 조회한다`() {
        // given
        val allMovies = movieRepository.getMovies().toList()
        val targetMovieId = requireNotNull(allMovies[0].id) { "Movie ID가 없습니다." }

        // when
        val movie = movieRepository.findById(targetMovieId)

        // then
        assertThat(movie).isNotNull
        assertThat(movie?.id).isEqualTo(targetMovieId)
        assertThat(movie?.title).isEqualTo("F1 더 무비")
    }
}
