package movie.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnectionFactory {
    private const val LOCAL_URL = "jdbc:h2:~/test"
    private const val TEST_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"

    fun createConnection(isLocal: Boolean): Connection {
        if (isLocal) return DriverManager.getConnection(LOCAL_URL, "sa", "")
        return DriverManager.getConnection(TEST_URL, "sa", "")
    }
}
