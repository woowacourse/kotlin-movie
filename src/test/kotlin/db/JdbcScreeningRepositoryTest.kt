package db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.DriverManager
import java.time.LocalDate
import java.util.UUID

class JdbcScreeningRepositoryTest {
    private lateinit var movieRepository: JdbcMovieRepository
    private lateinit var repository: JdbcScreeningRepository

    @BeforeEach
    fun setUp() {
        val connection = DriverManager.getConnection("jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1", "sa", "")
        DataInitializer(connection).initialize()
        movieRepository = JdbcMovieRepository(connection)
        repository = JdbcScreeningRepository(connection)
    }

    @Test
    fun `영화와 날짜로 상영 목록을 조회한다`() {
        val movieId = movieRepository.findIdByTitle("F1 더 무비")!!

        val screenings = repository.findByMovieIdAndDate(movieId, LocalDate.of(2025, 9, 20))

        assertThat(screenings).hasSize(4)
    }

    @Test
    fun `상영 기간 밖의 날짜는 빈 상영 목록을 반환한다`() {
        val movieId = movieRepository.findIdByTitle("F1 더 무비")!!

        val screenings = repository.findByMovieIdAndDate(movieId, LocalDate.of(2025, 10, 1))

        assertThat(screenings.isEmpty()).isTrue()
    }

    @Test
    fun `존재하지 않는 movie_id는 빈 상영 목록을 반환한다`() {
        val screenings = repository.findByMovieIdAndDate(999L, LocalDate.of(2025, 9, 20))

        assertThat(screenings.isEmpty()).isTrue()
    }
}
