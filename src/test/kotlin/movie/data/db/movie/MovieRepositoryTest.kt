package movie.data.db.movie

import movie.data.db.SchemaInitializer
import movie.domain.screening.Screenings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDateTime

class MovieRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setUp() {
        connection =
            DriverManager.getConnection(
                "jdbc:h2:mem:test-${System.nanoTime()}",
                "sa",
                "",
            )

        SchemaInitializer.initialize(connection)
        movieRepository = MovieRepository(connection)
    }

    @Test
    fun `영화 정보를 저장할 수 있다`() {
        val movie = createMovie(id = 1L, title = "F1 더 무비")

        movieRepository.save(movie, 120)

        connection.createStatement().use { statement ->
            statement.executeQuery("select id, title, running_time_minutes from movies").use { resultSet ->
                assertThat(resultSet.next()).isTrue()
                assertThat(resultSet.getLong("id")).isEqualTo(1L)
                assertThat(resultSet.getString("title")).isEqualTo("F1 더 무비")
                assertThat(resultSet.getInt("running_time_minutes")).isEqualTo(120)
            }
        }
    }

    @Test
    fun `저장된 모든 영화를 조회할 수 있다`() {
        val movie1 = createMovie(id = 1L, title = "F1 더 무비")
        val movie2 = createMovie(id = 2L, title = "토이 스토리")
        movieRepository.save(movie1, 120)
        movieRepository.save(movie2, 100)

        val movies = movieRepository.findAll()

        assertThat(movies).hasSize(2)
        assertThat(movies[0].id).isEqualTo(1L)
        assertThat(movies[0].title).isEqualTo("F1 더 무비")
        assertThat(movies[1].id).isEqualTo(2L)
        assertThat(movies[1].title).isEqualTo("토이 스토리")
    }

    @Test
    fun `영화 조회 시 해당 영화의 상영 정보도 함께 조회된다`() {
        val movie = createMovie(id = 1L, title = "F1 더 무비")
        movieRepository.save(movie, 120)
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

        val movies = movieRepository.findAll()

        assertThat(movies).hasSize(1)
        assertThat(movies[0].screenings.screenings).hasSize(2)
    }

    private fun createMovie(
        id: Long,
        title: String,
    ) = movie.domain.movie.Movie(
        id = id,
        title = title,
        screenings = Screenings(emptyList()),
    )

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
