package repository

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.Title
import domain.screening.ScreeningPeriod
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class MovieRepository(private val dataSource: DataSource) {

    fun findAll(): List<Movie> {
        val sql = "SELECT * FROM movies"
        val movies = mutableListOf<Movie>()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.executeQuery().use { rs ->
                    while (rs.next()) {
                        movies.add(
                            Movie(
                                id = rs.getLong("id"),
                                title = Title(rs.getString("title")),
                                runningTime = RunningTime(rs.getInt("running_time")),
                                screeningPeriod = ScreeningPeriod(
                                    startDate = rs.getDate("start_date").toLocalDate(),
                                    endDate = rs.getDate("end_date").toLocalDate()
                                )
                            )
                        )
                    }
                }
            }
        }
        return movies
    }

    fun findById(id: Long): Movie {
        val sql = "SELECT * FROM movies WHERE id = ?"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return Movie(
                            id = rs.getLong("id"),
                            title = Title(rs.getString("title")),
                            runningTime = RunningTime(rs.getInt("running_time")),
                            screeningPeriod = ScreeningPeriod(
                                startDate = rs.getDate("start_date").toLocalDate(),
                                endDate = rs.getDate("end_date").toLocalDate()
                            )
                        )
                    }
                }
            }
        }
        throw IllegalArgumentException("해당 ID의 영화를 찾을 수 없습니다: $id")
    }
}
