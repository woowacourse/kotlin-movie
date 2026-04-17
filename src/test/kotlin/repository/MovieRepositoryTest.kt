package repository

import db.DatabaseConfig
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

class MovieRepositoryTest {
    private val repository = MovieRepository()

    @BeforeEach
    fun setUp() {
        DatabaseConfig.configure("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        DatabaseConfig.initialize()
        clearTables()
    }

    @Test
    fun `존재하는 영화를 제목으로 조회하면 Movie 객체를 반환한다`() {
        insertMovie(title = "탑건: 매버릭", runningTime = 130)

        val movie = repository.findByTitle("탑건: 매버릭")

        assertThat(movie.title).isEqualTo("탑건: 매버릭")
        assertThat(movie.runningTime).isEqualTo(130L)
        assertThat(movie.startDate).isEqualTo(LocalDate.of(2026, 4, 1))
        assertThat(movie.endDate).isEqualTo(LocalDate.of(2026, 4, 30))
    }

    @Test
    fun `존재하지 않는 제목으로 조회하면 예외를 던진다`() {
        assertThatThrownBy { repository.findByTitle("없는영화") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("존재하지 않는 영화입니다")
    }

    private fun insertMovie(
        title: String,
        runningTime: Int,
        startDate: LocalDate = LocalDate.of(2026, 4, 1),
        endDate: LocalDate = LocalDate.of(2026, 4, 30),
    ) {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO MOVIE (title, running_time, start_date, end_date) VALUES (?, ?, ?, ?)",
                ).use { stmt ->
                    stmt.setString(1, title)
                    stmt.setInt(2, runningTime)
                    stmt.setDate(3, Date.valueOf(startDate))
                    stmt.setDate(4, Date.valueOf(endDate))
                    stmt.executeUpdate()
                }
        }
    }

    private fun clearTables() {
        DatabaseConfig.getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("DELETE FROM RESERVATION_ITEM")
                stmt.execute("DELETE FROM RESERVATION")
                stmt.execute("DELETE FROM SCREENING")
                stmt.execute("DELETE FROM MOVIE")
            }
        }
    }
}
