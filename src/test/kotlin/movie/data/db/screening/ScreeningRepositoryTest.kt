package movie.data.db.screening

import movie.data.db.SchemaInitializer
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDateTime

class ScreeningRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var screeningRepository: ScreeningRepository

    @BeforeEach
    fun setUp() {
        connection =
            DriverManager.getConnection(
                "jdbc:h2:mem:test-${System.nanoTime()}",
                "sa",
                "",
            )

        SchemaInitializer.initialize(connection)
        screeningRepository = ScreeningRepository(connection)

        insertMovie(
            id = 1L,
            title = "F1 더 무비",
            runningTimeMinutes = 120,
        )
    }

    @Test
    fun `상영 정보를 저장할 수 있다`() {
        val screening =
            Screening(
                id = 1L,
                screen = Screen(1),
                screeningDateTime =
                    ScreeningDateTime(
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 12, 0),
                    ),
                reservedSeats = ReservedSeats(Seats(emptySet())),
            )

        screeningRepository.save(
            movieId = 1L,
            screening = screening,
        )

        connection.createStatement().use { statement ->
            statement.executeQuery("select id, movie_id, screen_id, start_at, end_at from screenings").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getInt("id")).isEqualTo(1L)
                assertThat(resultSet.getInt("movie_id")).isEqualTo(1)
                assertThat(resultSet.getInt("screen_id")).isEqualTo(1)
                assertThat(resultSet.getTimestamp("start_at").toLocalDateTime())
                    .isEqualTo(LocalDateTime.of(2026, 1, 1, 10, 0))
                assertThat(resultSet.getTimestamp("end_at").toLocalDateTime())
                    .isEqualTo(LocalDateTime.of(2026, 1, 1, 12, 0))
            }
        }
    }

    @Test
    fun `영화 ID로 상영 정보를 조회할 수 있다`() {
        insertScreening(
            id = 1L,
            movieId = 1L,
            screenId = 1,
            startAt = LocalDateTime.of(2026, 1, 1, 10, 0),
            endAt = LocalDateTime.of(2026, 1, 1, 12, 0),
        )
        insertScreening(
            id = 2L,
            movieId = 1L,
            screenId = 1,
            startAt = LocalDateTime.of(2026, 1, 1, 14, 0),
            endAt = LocalDateTime.of(2026, 1, 1, 16, 0),
        )

        val screenings = screeningRepository.findAllByMovieId(1L)
        assertThat(screenings).hasSize(2)
        assertThat(screenings[0].id).isEqualTo(1L)
        assertThat(screenings[1].id).isEqualTo(2L)
    }

    private fun insertMovie(
        id: Long,
        title: String,
        runningTimeMinutes: Int,
    ) {
        val sql = "insert into movies(id, title, running_time_minutes) values (?, ?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, id)
            statement.setString(2, title)
            statement.setInt(3, runningTimeMinutes)
            statement.executeUpdate()
        }
    }

    private fun insertScreening(
        id: Long,
        movieId: Long,
        screenId: Int,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
    ) {
        val sql =
            """
            insert into screenings(id, movie_id, screen_id, start_at, end_at)
            values (?, ?, ?, ?, ?)
            """.trimIndent()

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, id)
            statement.setLong(2, movieId)
            statement.setInt(3, screenId)
            statement.setTimestamp(4, Timestamp.valueOf(startAt))
            statement.setTimestamp(5, Timestamp.valueOf(endAt))
            statement.executeUpdate()
        }
    }
}
