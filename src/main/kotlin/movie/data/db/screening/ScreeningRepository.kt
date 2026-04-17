package movie.data.db.screening

import movie.data.db.reservation.ReservedSeatRepository
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seats
import java.sql.Connection
import java.sql.Timestamp

class ScreeningRepository(
    private val connection: Connection,
) {
    private val reservedSeatRepository = ReservedSeatRepository(connection)

    fun save(
        movieId: Long,
        screening: Screening,
    ) {
        val sql =
            """
            insert into screenings(id, movie_id, screen_id, start_at, end_at)
            values (?, ?, ?, ?, ?)
            """.trimIndent()

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, screening.id)
            statement.setLong(2, movieId)
            statement.setInt(3, screening.screen.id)
            statement.setTimestamp(4, Timestamp.valueOf(screening.screeningDateTime.startAt))
            statement.setTimestamp(5, Timestamp.valueOf(screening.screeningDateTime.endAt))
            statement.executeUpdate()
        }
    }

    fun findAllByMovieId(movieId: Long): List<Screening> {
        val sql =
            """
            select id, movie_id, screen_id, start_at, end_at
            from screenings
            where movie_id = ?
            order by start_at
            """.trimIndent()

        val result = mutableListOf<Screening>()

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, movieId)

            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val screeningId = resultSet.getLong("id")
                    val reservedSeats = reservedSeatRepository.findByScreeningId(screeningId)

                    result.add(
                        Screening(
                            id = screeningId,
                            screen = Screen(resultSet.getInt("screen_id")),
                            screeningDateTime =
                                ScreeningDateTime(
                                    resultSet.getTimestamp("start_at").toLocalDateTime(),
                                    resultSet.getTimestamp("end_at").toLocalDateTime(),
                                ),
                            reservedSeats = ReservedSeats(Seats(reservedSeats)),
                        ),
                    )
                }
            }
        }
        return result
    }

    fun findById(screeningId: Long): Screening? {
        val sql =
            """
            select id, screen_id, start_at, end_at
            from screenings
            where id = ?
            """.trimIndent()

        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, screeningId)

            statement.executeQuery().use { resultSet ->
                if (!resultSet.next()) return null

                val reservedSeats = reservedSeatRepository.findByScreeningId(screeningId)

                return Screening(
                    id = screeningId,
                    screen = Screen(resultSet.getInt("screen_id")),
                    screeningDateTime =
                        ScreeningDateTime(
                            resultSet.getTimestamp("start_at").toLocalDateTime(),
                            resultSet.getTimestamp("end_at").toLocalDateTime(),
                        ),
                    reservedSeats = ReservedSeats(Seats(reservedSeats)),
                )
            }
        }
    }
}
