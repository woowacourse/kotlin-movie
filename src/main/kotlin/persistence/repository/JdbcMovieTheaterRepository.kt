package persistence.repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screen
import domain.cinema.ScreeningSchedule
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import persistence.ScreeningIdGenerator
import persistence.db.JdbcDatabase
import persistence.seed.FixedSeatLayout
import java.sql.Connection
import java.sql.Timestamp

internal class JdbcMovieTheaterRepository(
    private val database: JdbcDatabase,
) {
    fun initialize(movieTheater: MovieTheater) {
        database.withTransaction { connection ->
            if (hasScreenings(connection)) {
                return@withTransaction
            }
            insertMovies(connection, movieTheater)
            insertScreens(connection, movieTheater)
            insertScreenings(connection, movieTheater)
        }
    }

    fun find(): MovieTheater =
        database.withConnection { connection ->
            val movies = findMovies(connection)
            val reservedSeatsByScreeningId = findReservedSeats(connection)
            val screenings =
                findScreeningRows(connection).map { row ->
                    val screen =
                        Screen(
                            seats = FixedSeatLayout.createSeats(reservedSeatsByScreeningId[row.id].orEmpty()),
                            id = Id(row.screenId),
                        )
                    ScreeningSchedule(
                        startTime = row.startTime,
                        screen = screen,
                        movie = movies.getValue(row.movieId),
                    )
                }

            MovieTheater(
                screens = screenings.map(ScreeningSchedule::screen).distinctBy { it.id.value },
                movies = movies.values.sortedBy { it.id.value },
                screenings = screenings,
            )
        }

    private fun hasScreenings(connection: Connection): Boolean =
        connection.prepareStatement("SELECT COUNT(*) FROM screenings").use { statement ->
            statement.executeQuery().use { resultSet ->
                resultSet.next()
                resultSet.getInt(1) > 0
            }
        }

    private fun insertMovies(
        connection: Connection,
        movieTheater: MovieTheater,
    ) {
        connection.prepareStatement("INSERT INTO movies (id, title, running_time) VALUES (?, ?, ?)").use { statement ->
            movieTheater.movies.forEach { movie ->
                statement.setString(1, movie.id.value)
                statement.setString(2, movie.title)
                statement.setInt(3, movie.runningTime)
                statement.addBatch()
            }
            statement.executeBatch()
        }
    }

    private fun insertScreens(
        connection: Connection,
        movieTheater: MovieTheater,
    ) {
        connection.prepareStatement("INSERT INTO screens (id) VALUES (?)").use { statement ->
            movieTheater.screens.distinctBy { it.id.value }.forEach { screen ->
                statement.setString(1, screen.id.value)
                statement.addBatch()
            }
            statement.executeBatch()
        }
    }

    private fun insertScreenings(
        connection: Connection,
        movieTheater: MovieTheater,
    ) {
        connection
            .prepareStatement(
                "INSERT INTO screenings (id, movie_id, screen_id, start_time) VALUES (?, ?, ?, ?)",
            ).use { statement ->
                movieTheater.screenings.forEach { screening ->
                    statement.setString(1, ScreeningIdGenerator.generate(screening))
                    statement.setString(2, screening.movie.id.value)
                    statement.setString(3, screening.screen.id.value)
                    statement.setTimestamp(4, Timestamp.valueOf(screening.startTime.toJavaLocalDateTime()))
                    statement.addBatch()
                }
                statement.executeBatch()
            }
    }

    private fun findMovies(connection: Connection): Map<String, Movie> =
        connection
            .prepareStatement(
                "SELECT id, title, running_time FROM movies ORDER BY id",
            ).use { statement ->
                statement.executeQuery().use { resultSet ->
                    buildMap {
                        while (resultSet.next()) {
                            val movieId = resultSet.getString("id")
                            put(
                                movieId,
                                Movie(
                                    title = resultSet.getString("title"),
                                    id = Id(movieId),
                                    runningTime = resultSet.getInt("running_time"),
                                ),
                            )
                        }
                    }
                }
            }

    private fun findReservedSeats(connection: Connection): Map<String, Set<FixedSeatLayout.SeatKey>> =
        connection
            .prepareStatement(
                """
                SELECT screening_id, seat_row, seat_column
                FROM reservations
                ORDER BY screening_id, seat_row, seat_column
                """.trimIndent(),
            ).use { statement ->
                statement.executeQuery().use { resultSet ->
                    buildMap<String, MutableSet<FixedSeatLayout.SeatKey>> {
                        while (resultSet.next()) {
                            val screeningId = resultSet.getString("screening_id")
                            val reservedSeats = getOrPut(screeningId) { mutableSetOf() }
                            reservedSeats +=
                                FixedSeatLayout.SeatKey(
                                    row = resultSet.getString("seat_row").single(),
                                    column = resultSet.getInt("seat_column"),
                                )
                        }
                    }.mapValues { (_, seats) -> seats.toSet() }
                }
            }

    private fun findScreeningRows(connection: Connection): List<ScreeningRow> =
        connection
            .prepareStatement(
                """
                SELECT id, movie_id, screen_id, start_time
                FROM screenings
                ORDER BY start_time, id
                """.trimIndent(),
            ).use { statement ->
                statement.executeQuery().use { resultSet ->
                    buildList {
                        while (resultSet.next()) {
                            add(
                                ScreeningRow(
                                    id = resultSet.getString("id"),
                                    movieId = resultSet.getString("movie_id"),
                                    screenId = resultSet.getString("screen_id"),
                                    startTime = resultSet.getTimestamp("start_time").toKotlinLocalDateTime(),
                                ),
                            )
                        }
                    }
                }
            }

    private fun Timestamp.toKotlinLocalDateTime(): LocalDateTime = toLocalDateTime().toKotlinLocalDateTime()

    private data class ScreeningRow(
        val id: String,
        val movieId: String,
        val screenId: String,
        val startTime: LocalDateTime,
    )
}
