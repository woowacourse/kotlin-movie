package repository

import java.sql.Connection
import java.sql.DriverManager

object JdbcConnection {
    private const val URL = "jdbc:h2:./test;DB_CLOSE_DELAY=-1"
    private const val USER = "sa"
    private const val PASSWORD = ""

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
