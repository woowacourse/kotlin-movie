package persistence.db

import java.sql.Connection

internal class DatabaseInitializer(
    private val database: JdbcDatabase,
) {
    fun initialize() {
        database.withConnection { connection ->
            createMoviesTable(connection)
            createScreensTable(connection)
            createScreeningsTable(connection)
            createReservationsTable(connection)
            createReservationBatchesTable(connection)
            createReservationBatchItemsTable(connection)
        }
    }

    private fun createMoviesTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS movies (
                    id VARCHAR(255) PRIMARY KEY, 
                    title VARCHAR(255) NOT NULL, 
                    running_time INT NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    private fun createScreensTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS screens (
                    id VARCHAR(255) PRIMARY KEY
                )
                """.trimIndent(),
            )
        }
    }

    private fun createScreeningsTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS screenings (
                    id VARCHAR(255) PRIMARY KEY,
                    movie_id VARCHAR(255) NOT NULL,
                    screen_id VARCHAR(255) NOT NULL,
                    start_time TIMESTAMP NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    private fun createReservationsTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS reservations (
                    id VARCHAR(255) PRIMARY KEY,
                    screening_id VARCHAR(255) NOT NULL,
                    seat_row CHAR(1) NOT NULL,
                    seat_column INT NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    private fun createReservationBatchesTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS reservation_batches (
                    id VARCHAR(255) PRIMARY KEY,
                    used_points INT NOT NULL,
                    payment_method VARCHAR(255) NOT NULL,
                    total_price INT NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    private fun createReservationBatchItemsTable(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS reservation_batch_items (
                    reservation_batch_id VARCHAR(255) NOT NULL,
                    screening_id VARCHAR(255) NOT NULL,
                    seat_row CHAR(1) NOT NULL,
                    seat_column INT NOT NULL
                )
                """.trimIndent(),
            )
        }
    }
}
