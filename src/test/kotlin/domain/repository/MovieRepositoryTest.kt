package domain.repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.JdbcConnection
import repository.MovieRepository
import repository.SimpleDataSource
import java.sql.Connection

class MovieRepositoryTest {

    private lateinit var connection: Connection
    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setUp() {
        connection = JdbcConnection.getConnection()
        movieRepository = MovieRepository(SimpleDataSource())

        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS movies CASCADE")

            stmt.execute("""
                CREATE TABLE movies (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    running_time INT NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL
                )
            """.trimIndent())

            stmt.execute("""
                INSERT INTO movies (title, running_time, start_date, end_date) 
                VALUES ('커브볼', 148, '2026-01-01', '2026-12-31')
            """.trimIndent())
        }
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use {
            it.execute("DROP TABLE IF EXISTS movies CASCADE")
        }
        connection.close()
    }

    @Test
    fun `findAll은 저장된 모든 영화 목록을 반환한다`() {
        val movies = movieRepository.findAll()

        movies shouldHaveSize 1
        movies[0].title.title shouldBe "커브볼"
    }

    @Test
    fun `findById는 존재하는 ID로 조회 시 해당 영화를 반환한다`() {
        val movie = movieRepository.findById(1L)

        movie.id shouldBe 1L
        movie.title.title shouldBe "커브볼"
        movie.runningTime.duration shouldBe 148
    }

    @Test
    fun `findById는 존재하지 않는 ID로 조회 시 예외를 던진다`() {
        val invalidId = -1L

        val exception = shouldThrow<IllegalArgumentException> {
            movieRepository.findById(invalidId)
        }
        
        exception.message shouldBe "해당 ID의 영화를 찾을 수 없습니다: $invalidId"
    }
}
