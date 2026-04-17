package repository

import api.exception.ScreeningNotFoundException
import db.DatabaseConfig
import model.movie.Movie
import model.schedule.Screening
import model.seat.SeatInventory
import java.time.LocalDate

class ScreeningRepository {
    fun findByMovieAndDate(
        movie: Movie,
        date: LocalDate,
    ): List<Screening> {
        val sql =
            """
            SELECT s.id, s.start_time, s.end_time
            FROM SCREENING s
            JOIN MOVIE m ON s.movie_id = m.id
            WHERE m.title = ? AND CAST(s.start_time AS DATE) = ?
            """.trimIndent()

        val screenings = mutableListOf<Pair<Long, Screening>>()

        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setString(1, movie.title)
                stmt.setString(2, date.toString())
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val id = rs.getLong("id")
                    val startTime =
                        rs.getTimestamp("start_time").toLocalDateTime()
                    val screening =
                        Screening(
                            movie = movie,
                            startDateTime = startTime,
                            seatInventory =
                                SeatInventory.createDefaultSeatInventory(),
                        )
                    screenings.add(id to screening)
                }
            }
        }

        return screenings.map { (screeningId, screening) ->
            val reservedSeats =
                findReservedSeatNames(screeningId)
            if (reservedSeats.isEmpty()) {
                screening
            } else {
                screening.reserveSeats(reservedSeats)
            }
        }
    }

    fun findReservedSeatNames(screeningId: Long): List<String> {
        val sql = "SELECT seat_name FROM RESERVATION_ITEM WHERE screening_id = ?"
        val seatNames = mutableListOf<String>()

        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, screeningId)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    seatNames.add(rs.getString("seat_name"))
                }
            }
        }
        return seatNames
    }

    fun findIdByMovieAndStartTime(screening: Screening): Long {
        val sql =
            """
            SELECT s.id FROM SCREENING s
            JOIN MOVIE m ON s.movie_id = m.id
            WHERE m.title = ? AND s.start_time = ?
            """.trimIndent()

        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setString(1, screening.movie.title)
                stmt.setTimestamp(
                    2,
                    java.sql.Timestamp.valueOf(screening.startDateTime),
                )
                val rs = stmt.executeQuery()
                if (rs.next()) return rs.getLong("id")
            }
        }
        throw IllegalArgumentException("상영 정보를 찾을 수 없습니다.")
    }

    fun findByMovieId(
        movieId: Long,
        movie: Movie,
    ): List<Pair<Long, Screening>> {
        val sql = "SELECT id, start_time FROM SCREENING WHERE movie_id = ?"
        val result =
            mutableListOf<
                Pair<
                    Long,
                    Screening,
                >,
            >()

        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, movieId)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    result.add(
                        rs.getLong("id") to
                            Screening(
                                movie = movie,
                                startDateTime = rs.getTimestamp("start_time").toLocalDateTime(),
                                seatInventory =
                                    SeatInventory.createDefaultSeatInventory(),
                            ),
                    )
                }
            }
        }
        return result
    }

    fun findById(id: Long): Screening {
        val sql =
            """
                    SELECT s.start_time, m.title,          
            m.running_time, m.start_date, m.end_date
                    FROM SCREENING s JOIN MOVIE m ON       
            s.movie_id = m.id                              
                    WHERE s.id = ?                        
            """.trimIndent()

        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val movie =
                        Movie(
                            title =
                                rs.getString("title"),
                            runningTime =
                                rs.getInt("running_time").toLong(),
                            startDate =
                                rs.getDate("start_date").toLocalDate(),
                            endDate =
                                rs.getDate("end_date").toLocalDate(),
                        )
                    val screening =
                        Screening(
                            movie = movie,
                            startDateTime = rs.getTimestamp("start_time").toLocalDateTime(),
                            seatInventory =
                                SeatInventory.createDefaultSeatInventory(),
                        )
                    val reservedSeats =
                        findReservedSeatNames(id)
                    return if
                        (reservedSeats.isEmpty()) {
                        screening
                    } else {
                        screening.reserveSeats(reservedSeats)
                    }
                }
            }
        }
        throw ScreeningNotFoundException(id)
    }
}
