package repository

import db.DatabaseConfig
import model.movie.Movie

class MovieRepository {
    fun findByTitle(title: String): Movie {
        val sql = "SELECT * FROM MOVIE WHERE title = ?"
        DatabaseConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setString(1, title)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return Movie(
                        title = rs.getString("title"),
                        runningTime = rs.getInt("running_time").toLong(),
                        startDate = rs.getDate("start_date").toLocalDate(),
                        endDate = rs.getDate("end_date").toLocalDate(),
                    )
                }
            }
        }
        throw IllegalArgumentException("존재하지 않는 영화입니다")
    }

    fun findAll(): List<Pair<Long, Movie>> {
        val sql = "SELECT * FROM MOVIE"
        val result =
            mutableListOf<
                Pair<
                    Long,
                    Movie,
                >,
            >()

        DatabaseConfig.getConnection().use { connection ->
            connection.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    result.add(
                        rs.getLong("id") to
                            Movie(
                                title =
                                    rs.getString("title"),
                                runningTime =
                                    rs.getInt("running_time").toLong(),
                                startDate =
                                    rs.getDate("start_date").toLocalDate(),
                                endDate =
                                    rs.getDate("end_date").toLocalDate(),
                            ),
                    )
                }
            }
        }
        return result
    }
}
