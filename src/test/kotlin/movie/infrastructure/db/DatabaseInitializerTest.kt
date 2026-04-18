package movie.infrastructure.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection

class DatabaseInitializerTest {
    private lateinit var connection: Connection

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `데이터베이스에 연결할 수 있다`() {
        val result =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT 1").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `영화 데이터가 초기화된다`() {
        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM movie").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun `상영 데이터가 초기화된다`() {
        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM screening").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(7)
    }

    @Test
    fun `예약된 좌석 데이터가 초기화된다`() {
        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM reserved_seat").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun `초기 데이터가 이미 있으면 중복 삽입하지 않는다`() {
        DatabaseInitializer(connection).initialize()

        val count =
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM movie").use { rs ->
                    rs.next()
                    rs.getInt(1)
                }
            }
        assertThat(count).isEqualTo(3)
    }
}
