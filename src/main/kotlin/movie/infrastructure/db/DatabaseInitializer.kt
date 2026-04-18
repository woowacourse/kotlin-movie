package movie.infrastructure.db

import java.sql.Connection

class DatabaseInitializer(
    private val connection: Connection,
) {
    fun initialize() {
        createSchema()
        insertInitialData()
    }

    private fun createSchema() {
        val sql =
            this::class.java.classLoader
                .getResource("schema.sql")!!
                .readText()
        connection.createStatement().use { it.execute(sql) }
    }

    private fun insertInitialData() {
        val hasData =
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery("SELECT COUNT(*) FROM movie")
                resultSet.next()
                resultSet.getInt(1) > 0
            }

        if (hasData) return

        insertScreens()
        insertMovies()
        insertScreenings()
        insertReservedSeats()
    }

    private fun insertScreens() {
        connection.createStatement().use { it.execute("INSERT INTO screen (id) VALUES (1)") }
    }

    private fun insertMovies() {
        val sql = "INSERT INTO movie (id, title, running_time_minutes) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            listOf(
                Triple(1L, "F1 더 무비", 120),
                Triple(2L, "토이 스토리", 120),
                Triple(3L, "아이언맨", 120),
            ).forEach { (id, title, runningTime) ->
                statement.setLong(1, id)
                statement.setString(2, title)
                statement.setInt(3, runningTime)
                statement.executeUpdate()
            }
        }
    }

    private fun insertScreenings() {
        val sql = "INSERT INTO screening (id, movie_id, screen_id, start_at, end_at) VALUES (?, ?, ?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            listOf(
                listOf(101L, 1L, 1L, "2025-09-20 10:20:00", "2025-09-20 12:20:00"),
                listOf(102L, 1L, 1L, "2025-09-20 13:00:00", "2025-09-20 15:00:00"),
                listOf(103L, 1L, 1L, "2025-09-20 15:40:00", "2025-09-20 17:40:00"),
                listOf(104L, 1L, 1L, "2025-09-20 20:10:00", "2025-09-20 22:10:00"),
                listOf(201L, 2L, 1L, "2025-09-20 13:30:00", "2025-09-20 15:30:00"),
                listOf(202L, 2L, 1L, "2025-09-20 16:00:00", "2025-09-20 18:00:00"),
                listOf(301L, 3L, 1L, "2025-09-20 09:50:00", "2025-09-20 11:50:00"),
            ).forEach { row ->
                statement.setLong(1, row[0] as Long)
                statement.setLong(2, row[1] as Long)
                statement.setLong(3, row[2] as Long)
                statement.setString(4, row[3] as String)
                statement.setString(5, row[4] as String)
                statement.executeUpdate()
            }
        }
    }

    private fun insertReservedSeats() {
        val sql = "INSERT INTO reserved_seat (screening_id, seat_row, seat_column) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            // F1 더 무비 13:00 상영(id=102)에 이미 예약된 좌석
            listOf(
                Triple(102L, "B", 2),
                Triple(102L, "B", 3),
                Triple(102L, "C", 3),
                Triple(102L, "E", 4),
            ).forEach { (screeningId, row, column) ->
                statement.setLong(1, screeningId)
                statement.setString(2, row)
                statement.setInt(3, column)
                statement.executeUpdate()
            }
        }
    }
}
