package movie.data.db.movie

import movie.data.db.screening.ScreeningRepository
import movie.domain.movie.Movie
import movie.domain.screening.Screenings
import java.sql.Connection

class MovieRepository(
    private val connection: Connection,
) {
    fun save(
        movie: Movie,
        runningTimeMinutes: Int,
    ) {
        val sql = "insert into movies(id, title, running_time_minutes) values (?, ?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, movie.id)
            statement.setString(2, movie.title)
            statement.setInt(3, runningTimeMinutes)
            statement.executeUpdate()
        }
    }

    fun findAll(): List<Movie> {
        val sql = "select id, title, running_time_minutes from movies"
        val result = mutableListOf<Movie>()
        val screeningRepository = ScreeningRepository(connection)

        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { resultSet ->
                while (resultSet.next()) {
                    val id = resultSet.getLong("id")
                    val title = resultSet.getString("title")
                    val screenings = screeningRepository.findAllByMovieId(id)

                    result.add(
                        Movie(
                            id = id,
                            title = title,
                            screenings = Screenings(screenings),
                        ),
                    )
                }
            }
        }

        return result
    }

    fun isEmpty(): Boolean {
        val sql = "select count(*) from movies"

        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { resultSet ->
                resultSet.next()
                return resultSet.getInt(1) == 0
            }
        }
    }

    fun findRunningTimeMap(): Map<Long, Int> {
        val sql = "select id, running_time_minutes from movies"
        val map = mutableMapOf<Long, Int>()

        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { resultSet ->
                while (resultSet.next()) {
                    map[resultSet.getLong("id")] = resultSet.getInt("running_time_minutes")
                }
            }
        }

        return map
    }
}
