package db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.DriverManager
import java.util.UUID

class JdbcMovieRepositoryTest {
    private lateinit var repository: JdbcMovieRepository

    @BeforeEach
    fun setUp() {
        val connection = DriverManager.getConnection("jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1", "sa", "")
        DataInitializer(connection).initialize()
        repository = JdbcMovieRepository(connection)
    }

    @Test
    fun `전체 영화 목록을 조회한다`() {
        val movies = repository.findAll()

        assertThat(movies.findByTitle("F1 더 무비")).isNotNull()
        assertThat(movies.findByTitle("토이 스토리")).isNotNull()
        assertThat(movies.findByTitle("아이언맨")).isNotNull()
    }

    @Test
    fun `영화 제목으로 ID를 조회한다`() {
        val id = repository.findIdByTitle("F1 더 무비")

        assertThat(id).isNotNull()
    }

    @Test
    fun `존재하지 않는 영화 제목으로 조회하면 null을 반환한다`() {
        val id = repository.findIdByTitle("없는 영화")

        assertThat(id).isNull()
    }
}
