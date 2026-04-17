package movie.data.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.DriverManager

class DatabaseConnectionTest {
    @Test
    fun `H2 데이터베이스에 연결할 수 있다`() {
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
    }
}
