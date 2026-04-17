package db

import model.MockData
import java.sql.Date
import java.sql.Timestamp

object DataSeeder {
    fun seed() {
        DatabaseConfig.getConnection().use { connection ->
            val rs =
                connection.createStatement().executeQuery("SELECT COUNT(*) FROM MOVIE")
            if (rs.next() && rs.getInt(1) > 0) return

            MockData.movies.value.forEach { movie ->
                val movieSql = "INSERT INTO MOVIE (title, running_time, start_date, end_date) VALUES (?, ?, ?, ?)"
                val movieId: Long
                connection
                    .prepareStatement(
                        movieSql,
                        java.sql.Statement.RETURN_GENERATED_KEYS,
                    ).use { stmt ->
                        stmt.setString(1, movie.title)
                        stmt.setInt(
                            2,
                            movie.runningTime.toInt(),
                        )
                        stmt.setDate(
                            3,
                            Date.valueOf(movie.startDate),
                        )
                        stmt.setDate(
                            4,
                            Date.valueOf(movie.endDate),
                        )
                        stmt.executeUpdate()
                        val keys = stmt.generatedKeys
                        keys.next()
                        movieId = keys.getLong(1)
                    }

                MockData.mockSchedule.screenings
                    .filter {
                        it.movie.title ==
                            movie.title
                    }.forEach { screening ->
                        val screeningSql = "INSERT INTO SCREENING (movie_id, start_time, end_time) VALUES (?, ?, ?)"

                        connection.prepareStatement(screeningSql).use { stmt ->
                            stmt.setLong(1, movieId)
                            stmt.setTimestamp(
                                2,
                                Timestamp.valueOf(screening.startDateTime),
                            )
                            stmt.setTimestamp(
                                3,
                                Timestamp.valueOf(screening.endDateTime),
                            )
                            stmt.executeUpdate()
                        }
                    }
            }
        }
    }
}
