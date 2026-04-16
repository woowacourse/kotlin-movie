package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.model.movie.Movie
import movie.model.movie.Movies
import movie.model.movie.RunningTime
import movie.repository.MovieRepository

class JdbcMovieRepository(
    private val isLocal: Boolean,
) : MovieRepository {
    override fun getMovies(): Movies {
        val connection = DatabaseConnectionFactory.createConnection(isLocal = isLocal)
        val movies = mutableListOf<Movie>()

        connection.use { conn ->
            conn.createStatement().use { statement ->
                val sql = "SELECT * FROM movie"

                val resultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    val id = resultSet.getLong("id")
                    val title = resultSet.getString("title")
                    val runningTime = resultSet.getLong("running_time")

                    movies.add(
                        Movie(
                            id = id,
                            title = title,
                            runningTime = RunningTime(runningTime),
                        ),
                    )
                }
            }
        }
        return Movies(movies)
    }

    override fun findById(id: Long): Movie? {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            val sql = "SELECT * FROM movie WHERE id = ?"

            conn.prepareStatement(sql).use { pStatement ->
                pStatement.setLong(1, id)

                val resultSet = pStatement.executeQuery()

                if (resultSet.next()) {
                    return Movie(
                        id = resultSet.getLong("id"),
                        title = resultSet.getString("title"),
                        runningTime = RunningTime(resultSet.getLong("running_time")),
                    )
                }
            }
        }
        return null
    }

    override fun findByTitle(title: String): Movie? {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            val sql = "SELECT * FROM movie WHERE title = ?"

            conn.prepareStatement(sql).use { pStatement ->
                pStatement.setString(1, title)

                val resultSet = pStatement.executeQuery()

                if (resultSet.next()) {
                    return Movie(
                        id = resultSet.getLong("id"),
                        title = resultSet.getString("title"),
                        runningTime = RunningTime(resultSet.getLong("running_time")),
                    )
                }
            }
        }
        return null
    }
}
