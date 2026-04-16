package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.model.movie.Movie
import movie.model.screening.Screening
import movie.model.screening.ScreeningSeatMap
import movie.model.screening.Screenings
import movie.repository.MovieRepository
import movie.repository.ScreenRepository
import movie.repository.ScreeningRepository
import java.time.LocalDate

class JdbcScreeningRepository(
    private val isLocal: Boolean,
    private val screenRepository: ScreenRepository,
    private val movieRepository: MovieRepository,
) : ScreeningRepository {
    override fun findBy(
        movie: Movie,
        date: LocalDate,
    ): Screenings {
        val connection = DatabaseConnectionFactory.createConnection(isLocal = isLocal)
        val screenings = mutableListOf<Screening>()

        connection.use { conn ->
            val sql =
                """
                SELECT *
                FROM screening
                WHERE movie_id = ?
                AND CAST(start_date_time AS DATE) = ?
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                pStatement.setLong(
                    1,
                    requireNotNull(movie.id) { "DB에 저장된 영화 ID(${movie.id})를 찾을 수 없습니다." },
                )
                pStatement.setObject(2, date)

                val resultSet = pStatement.executeQuery()

                while (resultSet.next()) {
                    val id = resultSet.getLong("id")
                    val startDateTime = resultSet.getTimestamp("start_date_time").toLocalDateTime()
                    val screenName = resultSet.getString("screen_name")

                    val screen =
                        requireNotNull(screenRepository.findByName(screenName)) {
                            "DB에 저장된 상영관 이름($screenName)을 찾을 수 없습니다."
                        }

                    screenings.add(
                        Screening(
                            id = id,
                            movie = movie,
                            startDateTime = startDateTime,
                            seatMap = ScreeningSeatMap(screen),
                        ),
                    )
                }
            }
        }
        return Screenings(screenings)
    }

    override fun findById(id: Long): Screening? {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            val sql =
                """
                SELECT *
                FROM screening
                WHERE id = ?
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                pStatement.setLong(1, id)

                val resultSet = pStatement.executeQuery()

                if (resultSet.next()) {
                    val movieId = resultSet.getLong("movie_id")
                    val movie =
                        requireNotNull(movieRepository.findById(movieId)) {
                            "영화 DB에 영화 아이디($movieId)인 영화가 없습니다."
                        }
                    val startDateTime = resultSet.getTimestamp("start_date_time").toLocalDateTime()
                    val screenName = resultSet.getString("screen_name")
                    val screen =
                        requireNotNull(screenRepository.findByName(screenName)) {
                            "스크린 DB에 해당 스크린 이름($screenName)을 가진 스크린이 없습니다."
                        }

                    return Screening(
                        id = id,
                        movie = movie,
                        startDateTime = startDateTime,
                        seatMap = ScreeningSeatMap(screen),
                    )
                }
            }
        }
        return null
    }

    override fun getAllScreenings(): Screenings {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)
        val screenings = mutableListOf<Screening>()

        connection.use { conn ->
            conn.createStatement().use { statement ->
                val sql = "SELECT * FROM screening"

                val resultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    val id = resultSet.getLong("id")
                    val movieId = resultSet.getLong("movie_id")
                    val startDateTime = resultSet.getTimestamp("start_date_time").toLocalDateTime()
                    val screenName = resultSet.getString("screen_name")
                    val movie =
                        requireNotNull(movieRepository.findById(movieId)) { "movie DB에 해당 아이디($movieId)를 가진 영화가 없습니다." }
                    val screen =
                        requireNotNull(screenRepository.findByName(screenName)) { "screen DB 에 해당 스크린 이름($screenName)이 없습니다." }

                    screenings.add(
                        Screening(
                            id = id,
                            movie = movie,
                            startDateTime = startDateTime,
                            seatMap = ScreeningSeatMap(screen),
                        ),
                    )
                }
            }
        }
        return Screenings(screenings)
    }
}
