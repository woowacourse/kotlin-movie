package movie.data.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.DriverManager

class SchemaInitializerTest {
    @Test
    fun `스키마를 초기화하면 movies 테이블이 생성된다`() {
        val connection =
            DriverManager.getConnection(
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "sa",
                "",
            )
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT 1")
        if (resultSet.next()) {
            val result = resultSet.getInt(1)
            assertThat(result).isEqualTo(1)
        }
        connection.use {
            SchemaInitializer.initialize(connection)

            val resultSet = it.metaData.getTables(null, null, "MOVIES", null)
            resultSet.use { tables ->
                assertThat(tables.next()).isTrue
            }
        }
    }
}
