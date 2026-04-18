package api.service

import api.dto.MovieResponse
import api.dto.MoviesResponse
import api.dto.ScreeningResponse
import java.sql.Connection

class MovieQueryService(
    private val connection: Connection,
) {
    fun findAll(): MoviesResponse {
        val sql =
            """
            SELECT m.id AS movie_id, m.title, m.running_time_minutes,
                   s.id AS screening_id, s.start_date_time
            FROM movies m
            LEFT JOIN screenings s ON s.movie_id = m.id
            ORDER BY m.id, s.start_date_time
            """.trimIndent()
        val rs = connection.createStatement().executeQuery(sql)

        data class Row(
            val id: Long,
            val title: String,
            val minutes: Long,
            val screenings: MutableList<ScreeningResponse> = mutableListOf(),
        )

        val map = linkedMapOf<Long, Row>()
        while (rs.next()) {
            val movieId = rs.getLong("movie_id")
            val minutes = rs.getLong("running_time_minutes")
            val row = map.getOrPut(movieId) { Row(movieId, rs.getString("title"), minutes) }
            val screeningId = rs.getLong("screening_id")
            if (screeningId != 0L) {
                val startAt = rs.getTimestamp("start_date_time").toLocalDateTime()
                row.screenings.add(ScreeningResponse(screeningId, startAt, startAt.plusMinutes(minutes)))
            }
        }
        return MoviesResponse(map.values.map { MovieResponse(it.id, it.title, it.minutes, it.screenings) })
    }
}
