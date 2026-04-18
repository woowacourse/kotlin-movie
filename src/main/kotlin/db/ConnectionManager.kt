package db

import java.sql.Connection
import java.sql.DriverManager

object ConnectionManager {
    private const val LOCAL_URL = "jdbc:h2:~/kotlin-movie"
    const val TEST_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"

    fun localConnection(): Connection = DriverManager.getConnection(LOCAL_URL, "sa", "")

    fun testConnection(): Connection = DriverManager.getConnection(TEST_URL, "sa", "")
}
