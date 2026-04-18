package repository

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.Title
import domain.screening.Screening
import domain.screening.ScreeningPeriod
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.seat.*
import domain.common.TimeRange
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet
import javax.sql.DataSource

@Repository
class ScreeningRepository(
    private val dataSource: DataSource
) {
    fun findAll(): List<Screening> {
        val sql = """
            SELECT s.id as s_id, s.start_time, 
                   m.id as m_id, m.title, m.running_time, m.start_date, m.end_date,
                   r.id as r_id, r.name, r.operating_start_time, r.operating_end_time
            FROM screenings s
            JOIN movies m ON s.movie_id = m.id
            JOIN screening_rooms r ON s.room_id = r.id
        """.trimIndent()
        
        val screenings = mutableListOf<Screening>()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        val screeningId = rs.getLong("s_id")
                        val movie = mapMovie(rs)
                        val room = mapRoom(rs)
                        val startTime = rs.getTimestamp("start_time").toLocalDateTime()

                        val reservedSeats = findReservedSeats(connection, screeningId)
                        var seats = room.seats
                        reservedSeats.forEach { position ->
                            seats = seats.updateState(position, ReserveState.RESERVED)
                        }

                        screenings.add(
                            Screening(
                                id = screeningId,
                                movie = movie,
                                room = room,
                                startTime = startTime,
                                seats = seats
                            )
                        )
                    }
                }
            }
        }
        return screenings
    }

    fun findById(id: Long): Screening {
        val sql = """
            SELECT s.id as s_id, s.start_time, 
                   m.id as m_id, m.title, m.running_time, m.start_date, m.end_date,
                   r.id as r_id, r.name, r.operating_start_time, r.operating_end_time
            FROM screenings s
            JOIN movies m ON s.movie_id = m.id
            JOIN screening_rooms r ON s.room_id = r.id
            WHERE s.id = ?
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        val movie = mapMovie(rs)
                        val room = mapRoom(rs)
                        val startTime = rs.getTimestamp("start_time").toLocalDateTime()

                        val reservedSeats = findReservedSeats(connection, id)
                        var seats = room.seats
                        reservedSeats.forEach { position ->
                            seats = seats.updateState(position, ReserveState.RESERVED)
                        }

                        return Screening(
                            id = id,
                            movie = movie,
                            room = room,
                            startTime = startTime,
                            seats = seats
                        )
                    }
                }
            }
        }
        throw IllegalArgumentException("해당 ID의 상영 정보를 찾을 수 없습니다: $id")
    }

    private fun findReservedSeats(connection: Connection, screeningId: Long): List<SeatPosition> {
        val sql = "SELECT seat_row, seat_column FROM reserved_seats WHERE screening_id = ?"
        val positions = mutableListOf<SeatPosition>()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, screeningId)
            stmt.executeQuery().use { rs ->
                while (rs.next()) {
                    positions.add(
                        SeatPosition(
                            Row(rs.getString("seat_row")),
                            Column(rs.getInt("seat_column"))
                        )
                    )
                }
            }
        }
        return positions
    }

    private fun mapMovie(rs: ResultSet): Movie {
        return Movie(
            id = rs.getLong("m_id"),
            title = Title(rs.getString("title")),
            runningTime = RunningTime(rs.getInt("running_time")),
            screeningPeriod = ScreeningPeriod(
                startDate = rs.getDate("start_date").toLocalDate(),
                endDate = rs.getDate("end_date").toLocalDate()
            )
        )
    }

    private fun mapRoom(rs: ResultSet): ScreeningRoom {
        return ScreeningRoom(
            id = rs.getLong("r_id"),
            name = ScreeningRoomName(rs.getString("name")),
            operatingTime = TimeRange(
                rs.getTime("operating_start_time").toLocalTime(),
                rs.getTime("operating_end_time").toLocalTime()
            )
        )
    }
}
