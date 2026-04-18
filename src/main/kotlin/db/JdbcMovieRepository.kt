package db

import model.movie.Movie
import model.movie.Movies
import model.movie.RunningTime
import model.movie.ShowingPeriod
import repository.MovieRepository
import java.sql.Connection

class JdbcMovieRepository(
    private val connection: Connection,
) : MovieRepository {
    override fun findAll(): Movies {
        val rs =
            connection.createStatement().executeQuery(
                "SELECT title, running_time_minutes, showing_period_start, showing_period_end FROM movies ORDER BY id",
            )
        val movies = mutableListOf<Movie>()
        while (rs.next()) {
            movies.add(
                Movie(
                    title = rs.getString("title"),
                    runningTime = RunningTime(rs.getLong("running_time_minutes")),
                    showingPeriod =
                        ShowingPeriod(
                            startDate = rs.getDate("showing_period_start").toLocalDate(),
                            endDate = rs.getDate("showing_period_end").toLocalDate(),
                        ),
                ),
            )
        }
        return Movies(movies)
    }

    override fun findIdByTitle(title: String): Long? {
        val stmt = connection.prepareStatement("SELECT id FROM movies WHERE title = ?")
        stmt.setString(1, title)
        val rs = stmt.executeQuery()
        return if (rs.next()) rs.getLong("id") else null
    }
}
