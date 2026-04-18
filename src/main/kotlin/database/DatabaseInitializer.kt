package database

import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object DatabaseInitializer {
    fun init(connection: Connection) {
        connection.use {
            createTables(it)
            insertInitialData(it)
        }
    }

    private fun createTables(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS MOVIE (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(100) NOT NULL,
                    running_time INT NOT NULL,
                    period_start TIMESTAMP NOT NULL,
                    period_end TIMESTAMP NOT NULL
                )
                """.trimIndent(),
            )

            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS SCREENING_ROOM (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR NOT NULL,
                    operating_start TIME NOT NULL,
                    operating_end TIME NOT NULL
                )
                """.trimIndent(),
            )

            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS SCREENING_ROOM_SEAT (
                    room_id INT NOT NULL,
                    "row" VARCHAR NOT NULL,
                    col INT NOT NULL
                )
                """.trimIndent(),
            )

            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS SCREENING (
                    id INT AUTO_INCREMENT(101) PRIMARY KEY,
                    movie_id INT NOT NULL,
                    room_id INT NOT NULL,
                    start_time TIMESTAMP NOT NULL
                )
                """.trimIndent(),
            )

            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS RESERVATION (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    screening_id INT NOT NULL
                )
                """.trimIndent(),
            )

            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS RESERVATION_SEAT (
                    reservation_id INT NOT NULL,
                    "row" VARCHAR NOT NULL,
                    col INT NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    private fun insertInitialData(connection: Connection) {
        val rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM MOVIE")
        if (rs.next() && rs.getInt(1) > 0) return

        val interstellarId = 1
        val oppenheimerId = 2
        val room1Id = 1
        val room2Id = 2
        val startDate = LocalDate.of(2025, 9, 1).atStartOfDay()
        val endDate = LocalDate.of(2025, 9, 30).atStartOfDay()

        val moviePs =
            connection.prepareStatement(
                "INSERT INTO MOVIE (title, running_time, period_start, period_end) VALUES (?, ?, ?, ?)",
            )
        listOf(
            "인터스텔라" to 169,
            "오펜하이머" to 180,
        ).forEach { (title, runningTime) ->
            moviePs.setString(1, title)
            moviePs.setInt(2, runningTime)
            moviePs.setObject(3, startDate)
            moviePs.setObject(4, endDate)
            moviePs.executeUpdate()
        }

        val roomPs =
            connection.prepareStatement(
                "INSERT INTO SCREENING_ROOM (name, operating_start, operating_end) VALUES (?, ?, ?)",
            )
        listOf("1관", "2관").forEach { name ->
            roomPs.setString(1, name)
            roomPs.setObject(2, LocalTime.of(9, 0))
            roomPs.setObject(3, LocalTime.of(22, 0))
            roomPs.executeUpdate()
        }

        val seatPs =
            connection.prepareStatement(
                "INSERT INTO SCREENING_ROOM_SEAT (room_id, \"row\", col) VALUES (?, ?, ?)",
            )
        listOf(room1Id, room2Id).forEach { roomId ->
            listOf("A", "B", "C", "D", "E").forEach { row ->
                (1..4).forEach { col ->
                    seatPs.setInt(1, roomId)
                    seatPs.setString(2, row)
                    seatPs.setInt(3, col)
                    seatPs.executeUpdate()
                }
            }
        }

        // SCREENING
        val screeningPs =
            connection.prepareStatement(
                "INSERT INTO SCREENING (movie_id, room_id, start_time) VALUES (?, ?, ?)",
            )
        screeningPs.setInt(1, interstellarId)
        screeningPs.setInt(2, room1Id)
        screeningPs.setObject(3, LocalDateTime.of(2025, 9, 20, 13, 30))
        screeningPs.executeUpdate()

        screeningPs.setInt(1, interstellarId)
        screeningPs.setInt(2, room1Id)
        screeningPs.setObject(3, LocalDateTime.of(2025, 9, 20, 18, 0))
        screeningPs.executeUpdate()

        screeningPs.setInt(1, oppenheimerId)
        screeningPs.setInt(2, room2Id)
        screeningPs.setObject(3, LocalDateTime.of(2025, 9, 20, 10, 0))
        screeningPs.executeUpdate()
    }
}
