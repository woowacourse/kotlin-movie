package database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConfig {
    private const val LOCAL_URL = "jdbc:h2:~/test"

    fun getConnection(url: String = LOCAL_URL): Connection = DriverManager.getConnection(url, "sa", "")
}
