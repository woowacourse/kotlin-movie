package database.repository

import api.dto.MovieResponse
import api.dto.ScreeningResponse
import database.Database
import database.default.DefaultScreenings
import model.CinemaTime
import model.CinemaTimeRange
import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import model.schedule.MovieScreening
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import java.sql.Timestamp
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieScreeningRepository(
    private val reservationRepository: ReservationRepository = ReservationRepository(),
) {
    fun save() {
        val sql =
            """
            MERGE INTO movie_screening (id, movie_id, screen_start, screen_end)
            KEY (movie_id, screen_start)
            VALUES (?,?, ?, ?)
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                DefaultScreenings.rows.forEach { row ->
                    preparedStatement.setInt(1, row.id)
                    preparedStatement.setInt(2, row.movieId)
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(row.startAt)))
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.parse(row.endAt)))
                    preparedStatement.addBatch()
                }
                preparedStatement.executeBatch()
            }
        }
    }

    fun findByScreeningId(screeningId: Int): MovieScreening? {
        val seatGroup =
            SeatGroup(
                seats =
                    listOf(
                        Seat(SeatRow("B"), SeatColumn(2), SeatGrade.S),
                        Seat(SeatRow("B"), SeatColumn(1), SeatGrade.B),
                        Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
                        Seat(SeatRow("A"), SeatColumn(1), SeatGrade.S),
                    ),
            )

        val sql =
            """                                                                                                                                                                                                    
            SELECT ms.id AS screening_id, m.name, m.running_time_minutes, ms.screen_start, ms.screen_end
            FROM movie m                                                                                                                                                                                           
            JOIN movie_screening ms ON m.id = ms.movie_id
            WHERE ms.id = ?                     
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setInt(1, screeningId)
                preparedStatement.executeQuery().use { result ->
                    if (!result.next()) return null
                    val movie =
                        Movie(
                            name = MovieName(result.getString("name")),
                            id = MovieId(Uuid.generateV7()),
                            runningTime = RunningTime(result.getInt("running_time_minutes")),
                        )
                    val reserved = reservationRepository.findByScreeningId(screeningId)
                    val reservedSeatSet =
                        reserved
                            .mapNotNull { seat ->
                                val row = seat.substring(0, 1)
                                val col = seat.substring(1).toInt()
                                seatGroup.getSeat(SeatRow(row), SeatColumn(col))
                            }.toSet()
                    return MovieScreening(
                        movie = movie,
                        screenTime =
                            CinemaTimeRange(
                                start = CinemaTime(result.getTimestamp("screen_start").toLocalDateTime()),
                                end = CinemaTime(result.getTimestamp("screen_end").toLocalDateTime()),
                            ),
                        seatGroup = seatGroup,
                        reservedSeats = reservedSeatSet,
                    )
                }
            }
        }
    }

    fun findScreeningId(
        name: String,
        screenStart: String,
    ): Int? {
        val sql =
            """
            SELECT ms.id
            FROM movie_screening ms
            JOIN movie m ON m.id = ms.movie_id
            WHERE m.name = ? AND ms.screen_start = ?
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setString(1, name)
                preparedStatement.setString(2, screenStart)
                preparedStatement.executeQuery().use { result ->
                    if (result.next()) return result.getInt("id")
                    return null
                }
            }
        }
    }

    fun findScreeningsByMovieName(name: String): List<MovieScreening>? {
        val seatGroup =
            SeatGroup(
                seats =
                    listOf(
                        Seat(SeatRow("B"), SeatColumn(2), SeatGrade.S),
                        Seat(SeatRow("B"), SeatColumn(1), SeatGrade.B),
                        Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
                        Seat(SeatRow("A"), SeatColumn(1), SeatGrade.S),
                    ),
            )
        val sql =
            """
            SELECT ms.id AS screening_id, m.name, m.running_time_minutes, ms.screen_start, ms.screen_end
            FROM movie m
            JOIN movie_screening ms ON m.id = ms.movie_id
            WHERE m.name = ?
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setString(1, name)
                preparedStatement.executeQuery().use { result ->
                    val screenings = mutableListOf<MovieScreening>()
                    while (result.next()) {
                        val screeningId = result.getInt("id")
                        val movie =
                            Movie(
                                name = MovieName(result.getString("name")),
                                id = MovieId(Uuid.generateV7()),
                                runningTime = RunningTime(result.getInt("running_time_minutes")),
                            )
                        val reserved = reservationRepository.findByScreeningId(screeningId)
                        val reservedSeatSet =
                            reserved
                                .mapNotNull { seat ->
                                    val row = seat.substring(0, 1)
                                    val col = seat.substring(1).toInt()
                                    seatGroup.getSeat(SeatRow(row), SeatColumn(col))
                                }.toSet()
                        screenings +=
                            MovieScreening(
                                movie = movie,
                                screenTime =
                                    CinemaTimeRange(
                                        start = CinemaTime(result.getTimestamp("screen_start").toLocalDateTime()),
                                        end = CinemaTime(result.getTimestamp("screen_end").toLocalDateTime()),
                                    ),
                                seatGroup = seatGroup,
                                reservedSeats = reservedSeatSet,
                            )
                    }
                    return screenings.ifEmpty { null }
                }
            }
        }
    }

    fun findAllWithScreenings(): List<MovieResponse> {
        val sql =
            """
            SELECT m.id AS movie_id, m.name, m.running_time_minutes, 
                   ms.id AS screening_id, ms.screen_start, ms.screen_end
            FROM movie m
            JOIN movie_screening ms ON m.id = ms.movie_id
            ORDER BY m.id, ms.screen_start
            """.trimIndent()

        Database.connection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.executeQuery().use { result ->
                    val moviesMap = linkedMapOf<Int, MovieResponse>()
                    while (result.next()) {
                        val movieId = result.getInt("movie_id")
                        val screening =
                            ScreeningResponse(
                                id = result.getInt("screening_id"),
                                startAt = result.getTimestamp("screen_start").toLocalDateTime().toString(),
                                endAt = result.getTimestamp("screen_end").toLocalDateTime().toString(),
                            )
                        val existing = moviesMap[movieId]
                        if (existing != null) {
                            moviesMap[movieId] =
                                existing.copy(
                                    screenings = existing.screenings + screening,
                                )
                        } else {
                            moviesMap[movieId] =
                                MovieResponse(
                                    id = movieId,
                                    title = result.getString("name"),
                                    runningTimeMinutes = result.getInt("running_time_minutes"),
                                    screenings = listOf(screening),
                                )
                        }
                    }
                    return moviesMap.values.toList()
                }
            }
        }
    }
}
