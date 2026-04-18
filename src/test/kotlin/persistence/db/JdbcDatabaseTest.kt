package persistence.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class JdbcDatabaseTest {
    @Test
    fun `H2 데이터베이스 연결이 정상적으로 생성된다`() {
        val database = JdbcDatabase.inMemory(UUID.randomUUID().toString())

        val actual =
            database.withConnection { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("select 1").use { resultSet ->
                        resultSet.next()
                        resultSet.getInt(1)
                    }
                }
            }

        assertThat(actual).isEqualTo(1)
    }

    @Test
    fun `테이블 초기화가 정상적으로 수행된다`() {
        val database = JdbcDatabase.inMemory(UUID.randomUUID().toString())

        DatabaseInitializer(database).initialize()

        val actual =
            database.withConnection { connection ->
                connection.createStatement().use { statement ->
                    val resultSet =
                        statement.executeQuery(
                            """
                            SELECT table_name
                            FROM information_schema.tables
                            WHERE table_schema = 'PUBLIC'
                            """.trimIndent(),
                        )

                    resultSet.use { resultSet ->
                        buildSet {
                            while (resultSet.next()) {
                                add(resultSet.getString("table_name"))
                            }
                        }
                    }
                }
            }

        assertThat(actual).contains("MOVIES", "SCREENS", "SCREENINGS", "RESERVATIONS")
    }
}
