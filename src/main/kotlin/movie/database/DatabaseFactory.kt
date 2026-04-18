package movie.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseFactory {
    fun getConnection(): Connection =
        DriverManager.getConnection("jdbc:h2:./movie_db;MODE=MySQL", "sa", "")
}
