@file:Suppress("NonAsciiCharacters")

package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.database.DatabaseInitializer
import movie.repository.inmemory.InMemoryScreenRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.use

class JdbcScreeningRepositoryTest {
    private val isLocal = false
    private val movieRepository = JdbcMovieRepository(isLocal)
    private val screeningRepository =
        JdbcScreeningRepository(
            isLocal = isLocal,
            screenRepository = InMemoryScreenRepository,
            movieRepository = movieRepository,
        )

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
    fun `특정 영화와 날짜를 조건으로 쿼리 시 올바른 상영 회차 정보들만 반환한다`() {
        // given
        val movie = requireNotNull(movieRepository.findByTitle("F1 더 무비")) { "F1 더 무비 영화가 없습니다." }
        val date = LocalDate.of(2025, 9, 20)

        // when
        val screenings = screeningRepository.findBy(movie, date).toList()

        // then: 기존 F1 더 무비 영화가 4편 로드되어 있기에 size 4값을 반환한다.
        assertThat(screenings).hasSize(4)
        screenings.forEach {
            assertThat(it.movie.title).isEqualTo("F1 더 무비")
            assertThat(it.startDateTime.toLocalDate()).isEqualTo(date)
        }
    }

    @Test
    fun `ID로 상영 회차를 조회한다`() {
        // given
        val movie = requireNotNull(movieRepository.findByTitle("아이언맨")) { "아이언맨 영화가 없습니다." }
        val date = LocalDate.of(2025, 9, 20)
        val screenings = screeningRepository.findBy(movie, date).toList()
        val targetId = requireNotNull(screenings[0].id) { "screening ID가 없습니다." }

        // when
        val screening = screeningRepository.findById(targetId)

        // then
        assertThat(screening).isNotNull
        assertThat(screening?.id).isEqualTo(targetId)
        assertThat(screening?.movie?.title).isEqualTo("아이언맨")
    }
}
