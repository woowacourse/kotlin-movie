package movie.infrastructure.db

import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.time.LocalDate

class JdbcMovieRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var movieRepository: JdbcMovieRepository

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()

        val reservedSeatRepository = JdbcReservedSeatRepository(connection)
        val screeningRepository = JdbcScreeningRepository(connection, reservedSeatRepository)
        movieRepository = JdbcMovieRepository(connection, screeningRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `전체 영화 목록을 조회할 수 있다`() {
        val movies = movieRepository.findAll()

        val movie = movies.findMovie("F1 더 무비")
        assertThat(movie.title.toString()).isEqualTo("F1 더 무비")
    }

    @Test
    fun `영화에 상영 목록이 포함된다`() {
        val movies = movieRepository.findAll()
        val movie = movies.findMovie("F1 더 무비")

        var count = 0
        movie
            .getScreeningsByDate(LocalDate.of(2025, 9, 20))
            .forEachIndexed { _, _ -> count++ }

        assertThat(count).isEqualTo(4)
    }

    @Test
    fun `존재하지 않는 영화를 찾으면 예외가 발생한다`() {
        val movies = movieRepository.findAll()

        val exception =
            assertThrows<IllegalArgumentException> {
                movies.findMovie("존재하지 않는 영화")
            }
        assertThat(exception.message).isEqualTo("영화를 찾을 수 없습니다.")
    }

    @Test
    fun `모든 영화가 조회된다`() {
        val movies = movieRepository.findAll()

        assertThat(movies.findMovie("F1 더 무비")).isNotNull()
        assertThat(movies.findMovie("토이 스토리")).isNotNull()
        assertThat(movies.findMovie("아이언맨")).isNotNull()
    }

    @Test
    fun `상영에 예약된 좌석 정보가 포함된다`() {
        val movies = movieRepository.findAll()
        val movie = movies.findMovie("F1 더 무비")
        val screenings = movie.getScreeningsByDate(LocalDate.of(2025, 9, 20))

        val screening = screenings.findByNumber(2)
        val testSeat = Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B)
        assertThat(screening.isSeatAvailable(testSeat)).isFalse()
    }
}
