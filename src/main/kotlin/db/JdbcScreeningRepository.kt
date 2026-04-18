package db

import model.Screen
import model.movie.Movie
import model.movie.RunningTime
import model.movie.ShowingPeriod
import model.screening.Screening
import model.screening.Screenings
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import repository.ScreeningRepository
import java.sql.Connection
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class JdbcScreeningRepository(
    private val connection: Connection,
) : ScreeningRepository {
    override fun findByMovieIdAndDate(
        movieId: Long,
        date: LocalDate,
    ): Screenings {
        val movie = findMovie(movieId) ?: return Screenings(emptyList())

        val stmt =
            connection.prepareStatement(
                """
                SELECT id, start_date_time, screen_name
                FROM screenings
                WHERE movie_id = ? AND CAST(start_date_time AS DATE) = ?
                ORDER BY start_date_time
                """.trimIndent(),
            )
        stmt.setLong(1, movieId)
        stmt.setDate(2, java.sql.Date.valueOf(date))
        val rs = stmt.executeQuery()

        val screenings = mutableListOf<Screening>()
        while (rs.next()) {
            val screeningId = rs.getLong("id")
            val startDateTime = rs.getTimestamp("start_date_time").toLocalDateTime()
            val screenName = rs.getString("screen_name")
            screenings.add(
                Screening(
                    movie = movie,
                    startDateTime = startDateTime,
                    screen = Screen(screenName, defaultSeats()),
                    reservedSeatNumbers = findReservedSeatNumbers(screeningId),
                ),
            )
        }
        return Screenings(screenings)
    }

    override fun findId(
        movieId: Long,
        startDateTime: LocalDateTime,
    ): Long? {
        val stmt =
            connection.prepareStatement(
                "SELECT id FROM screenings WHERE movie_id = ? AND start_date_time = ?",
            )
        stmt.setLong(1, movieId)
        stmt.setTimestamp(2, Timestamp.valueOf(startDateTime))
        val rs = stmt.executeQuery()
        return if (rs.next()) rs.getLong("id") else null
    }

    override fun findById(screeningId: Long): Screening? {
        val stmt =
            connection.prepareStatement(
                """
                SELECT s.id, s.start_date_time, s.screen_name,
                       m.title, m.running_time_minutes,
                       m.showing_period_start, m.showing_period_end
                FROM screenings s
                JOIN movies m ON s.movie_id = m.id
                WHERE s.id = ?
                """.trimIndent(),
            )
        stmt.setLong(1, screeningId)
        val rs = stmt.executeQuery()
        if (!rs.next()) return null
        val movie =
            Movie(
                title = rs.getString("title"),
                runningTime = RunningTime(rs.getLong("running_time_minutes")),
                showingPeriod =
                    ShowingPeriod(
                        startDate = rs.getDate("showing_period_start").toLocalDate(),
                        endDate = rs.getDate("showing_period_end").toLocalDate(),
                    ),
            )
        return Screening(
            movie = movie,
            startDateTime = rs.getTimestamp("start_date_time").toLocalDateTime(),
            screen = Screen(rs.getString("screen_name"), defaultSeats()),
            reservedSeatNumbers = findReservedSeatNumbers(screeningId),
        )
    }

    private fun findMovie(movieId: Long): Movie? {
        val stmt =
            connection.prepareStatement(
                "SELECT title, running_time_minutes, showing_period_start, showing_period_end FROM movies WHERE id = ?",
            )
        stmt.setLong(1, movieId)
        val rs = stmt.executeQuery()
        if (!rs.next()) return null
        return Movie(
            title = rs.getString("title"),
            runningTime = RunningTime(rs.getLong("running_time_minutes")),
            showingPeriod =
                ShowingPeriod(
                    startDate = rs.getDate("showing_period_start").toLocalDate(),
                    endDate = rs.getDate("showing_period_end").toLocalDate(),
                ),
        )
    }

    private fun findReservedSeatNumbers(screeningId: Long): Set<SeatNumber> {
        val stmt =
            connection.prepareStatement(
                "SELECT seat_row, seat_column FROM reservation_seats WHERE screening_id = ?",
            )
        stmt.setLong(1, screeningId)
        val rs = stmt.executeQuery()
        val seatNumbers = mutableSetOf<SeatNumber>()
        while (rs.next()) {
            seatNumbers.add(SeatNumber(rs.getString("seat_row")[0], rs.getInt("seat_column")))
        }
        return seatNumbers
    }

    private fun defaultSeats(): Seats =
        Seats(
            ('A'..'B').flatMap { row -> (1..4).map { col -> Seat(SeatNumber(row, col), SeatGrade.B) } } +
                ('C'..'D').flatMap { row -> (1..4).map { col -> Seat(SeatNumber(row, col), SeatGrade.S) } } +
                (1..4).map { col -> Seat(SeatNumber('E', col), SeatGrade.A) },
        )
}
