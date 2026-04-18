package movie.infrastructure.db

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnector {
    private const val LOCAL_URL = "jdbc:h2:~/movie"
    private const val TEST_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    private const val USER = "sa"
    private const val PASSWORD = ""

    fun connectLocal(): Connection = DriverManager.getConnection(LOCAL_URL, USER, PASSWORD)

    fun connectTest(): Connection = DriverManager.getConnection(TEST_URL, USER, PASSWORD)
}
