package movie.infrastructure.db

import movie.domain.movie.MovieTitle
import movie.domain.screening.Screen
import movie.domain.screening.ScreenId
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.ScreeningSlot
import movie.domain.screening.Screenings
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seats
import movie.repository.ReservedSeatRepository
import movie.repository.ScreeningRepository
import java.sql.Connection
import java.sql.ResultSet

class JdbcScreeningRepository(
    private val connection: Connection,
    private val reservedSeatRepository: ReservedSeatRepository,
) : ScreeningRepository {
    override fun findById(id: Long): Screening {
        val sql =
            """
            SELECT s.id, s.screen_id, s.start_at, s.end_at, m.title
            FROM screening s
            JOIN movie m ON s.movie_id = m.id
            WHERE s.id = ?
            """.trimIndent()

        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, id)
            stmt.executeQuery().use { rs ->
                require(rs.next()) { "상영 정보를 찾을 수 없습니다." }
                return toScreening(rs)
            }
        }
    }

    override fun findAllByMovieId(movieId: Long): Screenings {
        val sql =
            """
            SELECT s.id, s.screen_id, s.start_at, s.end_at, m.title
            FROM screening s
            JOIN movie m ON s.movie_id = m.id
            WHERE s.movie_id = ?
            ORDER BY s.start_at
            """.trimIndent()

        val screenings = mutableListOf<Screening>()

        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, movieId)
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    screenings.add(toScreening(rs))
                }
            }
        }

        return Screenings(screenings)
    }

    private fun toScreening(rs: ResultSet): Screening {
        val screeningId = rs.getLong("id")
        val screenId = rs.getInt("screen_id")
        val startAt = rs.getTimestamp("start_at").toLocalDateTime()
        val endAt = rs.getTimestamp("end_at").toLocalDateTime()
        val title = rs.getString("title")

        val reservedSeats = reservedSeatRepository.findAllByScreeningId(screeningId)

        return Screening(
            id = screeningId,
            movie = MovieTitle(title),
            slot =
                ScreeningSlot(
                    Screen(ScreenId(screenId), Seats.createDefault()),
                    ScreeningDateTime(
                        startAt.toLocalDate(),
                        startAt.toLocalTime(),
                        endAt.toLocalTime(),
                    ),
                ),
            reservatedSeats = ReservatedSeats(reservedSeats),
        )
    }
}
