package movie.data.db

import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {
    var url = "jdbc:h2:./movie-db"
    private const val USER = "sa"
    private const val PASSWORD = ""

    val connection: Connection
        get() = DriverManager.getConnection(url, USER, PASSWORD)
}
