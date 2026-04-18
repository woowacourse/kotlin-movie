package persistence.repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.Screen
import domain.cinema.ScreeningSchedule
import domain.reservation.ReservationInfo
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import persistence.db.JdbcDatabase
import persistence.seed.FixedSeatLayout
import java.sql.Connection
import java.sql.Timestamp

internal class JdbcReservationRepository(
    private val database: JdbcDatabase,
) {
    fun saveAll(reservationInfos: List<ReservationInfo>) {
        if (reservationInfos.isEmpty()) return

        database.withTransaction { connection ->
            saveAll(connection, reservationInfos)
        }
    }

    fun saveAll(
        connection: Connection,
        reservationInfos: List<ReservationInfo>,
    ) {
        if (reservationInfos.isEmpty()) return

        val screeningIds = findScreeningIds(connection, reservationInfos)
        connection
            .prepareStatement(
                "INSERT INTO reservations (id, screening_id, seat_row, seat_column) VALUES (?, ?, ?, ?)",
            ).use { statement ->
                reservationInfos.forEach { reservationInfo ->
                    val screeningKey = ScreeningKey.from(reservationInfo.screening)
                    val screeningId = screeningIds.getValue(screeningKey)

                    statement.setString(1, reservationIdOf(screeningId, reservationInfo))
                    statement.setString(
                        2,
                        screeningId,
                    )
                    statement.setString(
                        3,
                        reservationInfo.seat.coordinate.row
                            .toString(),
                    )
                    statement.setInt(4, reservationInfo.seat.coordinate.column)
                    statement.addBatch()
                }
                statement.executeBatch()
            }
    }

    fun findAll(): List<ReservationInfo> =
        database.withConnection { connection ->
            connection
                .prepareStatement(
                    """
                    SELECT
                        r.seat_row,
                        r.seat_column,
                        s.movie_id,
                        s.screen_id,
                        s.start_time,
                        m.title,
                        m.running_time
                    FROM reservations r
                    INNER JOIN screenings s ON s.id = r.screening_id
                    INNER JOIN movies m ON m.id = s.movie_id
                    ORDER BY s.start_time, r.seat_row, r.seat_column
                    """.trimIndent(),
                ).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        buildList {
                            val screenings = mutableMapOf<ScreeningKey, ScreeningSchedule>()

                            while (resultSet.next()) {
                                val screeningKey =
                                    ScreeningKey(
                                        movieId = resultSet.getString("movie_id"),
                                        screenId = resultSet.getString("screen_id"),
                                        startTime = resultSet.getTimestamp("start_time").toKotlinLocalDateTime(),
                                    )
                                val screening =
                                    screenings.getOrPut(screeningKey) {
                                        ScreeningSchedule(
                                            startTime = screeningKey.startTime,
                                            screen = Screen(FixedSeatLayout.createSeats(), Id(screeningKey.screenId)),
                                            movie =
                                                Movie(
                                                    title = resultSet.getString("title"),
                                                    id = Id(screeningKey.movieId),
                                                    runningTime = resultSet.getInt("running_time"),
                                                ),
                                        )
                                    }

                                val seatKey =
                                    FixedSeatLayout.SeatKey(
                                        row = resultSet.getString("seat_row").single(),
                                        column = resultSet.getInt("seat_column"),
                                    )
                                add(
                                    ReservationInfo(
                                        screening = screening,
                                        seat =
                                            Seat(
                                                coordinate = SeatCoordinate(seatKey.row, seatKey.column),
                                                grade = FixedSeatLayout.findSeatGrade(seatKey),
                                                isReserved = SeatState.RESERVED,
                                            ),
                                    ),
                                )
                            }
                        }
                    }
                }
        }

    private fun findScreeningIds(
        connection: Connection,
        reservationInfos: List<ReservationInfo>,
    ): Map<ScreeningKey, String> {
        val screeningKeys = reservationInfos.map { ScreeningKey.from(it.screening) }.distinct()
        return screeningKeys.associateWith { screeningKey ->
            connection
                .prepareStatement(
                    """
                    SELECT id
                    FROM screenings
                    WHERE movie_id = ? AND screen_id = ? AND start_time = ?
                    """.trimIndent(),
                ).use { statement ->
                    statement.setString(1, screeningKey.movieId)
                    statement.setString(2, screeningKey.screenId)
                    statement.setTimestamp(3, Timestamp.valueOf(screeningKey.startTime.toJavaLocalDateTime()))
                    statement.executeQuery().use { resultSet ->
                        require(resultSet.next())
                        resultSet.getString("id")
                    }
                }
        }
    }

    private fun Timestamp.toKotlinLocalDateTime(): LocalDateTime = toLocalDateTime().toKotlinLocalDateTime()

    private fun reservationIdOf(
        screeningId: String,
        reservationInfo: ReservationInfo,
    ): String = "reservation-$screeningId-${reservationInfo.seat.coordinate.row}${reservationInfo.seat.coordinate.column}"

    private data class ScreeningKey(
        val movieId: String,
        val screenId: String,
        val startTime: LocalDateTime,
    ) {
        companion object {
            fun from(screening: ScreeningSchedule): ScreeningKey =
                ScreeningKey(
                    movieId = screening.movie.id.value,
                    screenId = screening.screen.id.value,
                    startTime = screening.startTime,
                )
        }
    }
}
