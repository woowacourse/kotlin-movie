package db

import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DataInitializer(
    private val connection: Connection,
) {
    fun initialize() {
        createSchema()
        if (isMoviesEmpty()) {
            val movieIds = insertMovies()
            insertScreenings(movieIds)
        }
    }

    private fun createSchema() {
        val schema =
            javaClass
                .getResourceAsStream("/schema.sql")
                ?.bufferedReader()
                ?.readText()
                ?: error("schema.sql을 찾을 수 없습니다.")
        schema
            .split(";")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .forEach { connection.createStatement().execute(it) }
    }

    private fun isMoviesEmpty(): Boolean {
        val rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM movies")
        return rs.next() && rs.getInt(1) == 0
    }

    private fun insertMovies(): Map<String, Long> {
        val stmt =
            connection.prepareStatement(
                "INSERT INTO movies (title, running_time_minutes, showing_period_start, showing_period_end) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS,
            )
        val start = java.sql.Date.valueOf(LocalDate.of(2025, 9, 1))
        val end = java.sql.Date.valueOf(LocalDate.of(2025, 9, 30))
        val movieIds = mutableMapOf<String, Long>()

        listOf("F1 더 무비" to 160L, "토이 스토리" to 150L, "아이언맨" to 130L).forEach { (title, runningTime) ->
            stmt.setString(1, title)
            stmt.setLong(2, runningTime)
            stmt.setDate(3, start)
            stmt.setDate(4, end)
            stmt.executeUpdate()
            val rs = stmt.generatedKeys
            if (rs.next()) movieIds[title] = rs.getLong(1)
        }
        return movieIds
    }

    private fun insertScreenings(movieIds: Map<String, Long>) {
        val screeningData =
            mapOf(
                "F1 더 무비" to
                    Pair(
                        "1관",
                        listOf(LocalTime.of(10, 20), LocalTime.of(13, 0), LocalTime.of(15, 40), LocalTime.of(20, 10)),
                    ),
                "토이 스토리" to Pair("2관", listOf(LocalTime.of(13, 30), LocalTime.of(16, 0))),
                "아이언맨" to Pair("3관", listOf(LocalTime.of(9, 50))),
            )

        val stmt = connection.prepareStatement("INSERT INTO screenings (movie_id, start_date_time, screen_name) VALUES (?, ?, ?)")
        val periodStart = LocalDate.of(2025, 9, 1)
        val periodEnd = LocalDate.of(2025, 9, 30)

        screeningData.forEach { (title, pair) ->
            val (screenName, times) = pair
            val movieId = movieIds[title] ?: return@forEach
            generateSequence(periodStart) { it.plusDays(1) }
                .takeWhile { !it.isAfter(periodEnd) }
                .forEach { date ->
                    times.forEach { time ->
                        stmt.setLong(1, movieId)
                        stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(date, time)))
                        stmt.setString(3, screenName)
                        stmt.addBatch()
                    }
                }
        }
        stmt.executeBatch()
    }
}
