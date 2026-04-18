package movie.repository

import movie.dto.MovieScheduleDto
import movie.dto.ReservedSeatDto
import java.sql.Connection

class ScheduleRepository(private val connection: Connection) {
    fun findAllSchedule(): List<MovieScheduleDto> {
        val schedules = fetchMovieSchedules()
        val allReservedSeats = fetchAllReservedSeats()

        return schedules.map { schedule ->
            schedule.copy(
                reservedSeats = allReservedSeats[schedule.scheduleId] ?: emptyList()
            )
        }
    }

    private fun fetchMovieSchedules(): List<MovieScheduleDto> {
        val query = """
            SELECT m.id as movie_id, s.id as schedule_id, m.title, m.running_time, s.start_time, s.end_time 
            FROM movie m 
            JOIN schedule s ON m.id = s.movie_id
        """.trimIndent()

        val movieSchedules = mutableListOf<MovieScheduleDto>()
        connection.prepareStatement(query).use { pstmt ->
            val rs = pstmt.executeQuery()

            while (rs.next()) {
                movieSchedules.add(
                    MovieScheduleDto(
                        movieId = rs.getLong("movie_id"),
                        scheduleId = rs.getLong("schedule_id"),
                        title = rs.getString("title"),
                        runningTime = rs.getInt("running_time"),
                        startTime = rs.getTimestamp("start_time").toLocalDateTime(),
                        endTime = rs.getTimestamp("end_time").toLocalDateTime()
                    )
                )
            }
        }
        return movieSchedules
    }

    private fun fetchAllReservedSeats(): Map<Long, List<ReservedSeatDto>> {
        val query = """
            SELECT schedule_id, seat_number
            FROM reserved_seat
        """.trimIndent()
        val seatMap = mutableMapOf<Long, MutableList<ReservedSeatDto>>()

        connection.prepareStatement(query).use { pstmt ->
            val rs = pstmt.executeQuery()

            while (rs.next()) {
                val id = rs.getLong("schedule_id")
                val seat = ReservedSeatDto(rs.getString("seat_number"))
                seatMap.getOrPut(id) { mutableListOf() }.add(seat)
            }
        }
        return seatMap
    }
}
