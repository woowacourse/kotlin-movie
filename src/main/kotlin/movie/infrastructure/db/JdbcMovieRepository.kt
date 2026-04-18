package movie.infrastructure.db

import movie.domain.movie.Movie
import movie.domain.movie.MovieTitle
import movie.domain.movie.Movies
import movie.repository.MovieRepository
import movie.repository.ScreeningRepository
import java.sql.Connection

class JdbcMovieRepository(
    private val connection: Connection,
    private val screeningRepository: ScreeningRepository,
) : MovieRepository {
    override fun findAll(): Movies {
        val sql = "SELECT id, title, running_time_minutes FROM movie ORDER BY id"
        val movies = mutableListOf<Movie>()

        connection.prepareStatement(sql).use { stmt ->
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    val movieId = rs.getLong("id")
                    val title = rs.getString("title")
                    val runningTimeMinutes = rs.getInt("running_time_minutes")
                    val screenings = screeningRepository.findAllByMovieId(movieId)

                    movies.add(
                        Movie(
                            id = movieId,
                            title = MovieTitle(title),
                            screenings = screenings,
                            runningTimeMinutes = runningTimeMinutes,
                        ),
                    )
                }
            }
        }

        return Movies(movies)
    }
}
