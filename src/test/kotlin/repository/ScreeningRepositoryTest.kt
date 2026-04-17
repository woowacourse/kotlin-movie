package repository

import db.DatabaseConfig
import model.movie.Movie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class ScreeningRepositoryTest {
    private val repository = ScreeningRepository()

    @BeforeEach
    fun setUp() {
        DatabaseConfig.configure("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        DatabaseConfig.initialize()
        clearTables()
    }

    @Test
    fun `영화와 날짜로 상영 목록을 조회한다`() {
        val movieId = insertMovie("탑건: 매버릭")
        insertScreening(movieId, LocalDateTime.of(2026, 4, 17, 10, 0), LocalDateTime.of(2026, 4, 17, 12, 10))
        insertScreening(movieId, LocalDateTime.of(2026, 4, 17, 14, 0), LocalDateTime.of(2026, 4, 17, 16, 10))

        val movie = Movie("탑건: 매버릭", 130, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))
        val screenings = repository.findByMovieAndDate(movie, LocalDate.of(2026, 4, 17))

        assertThat(screenings).hasSize(2)
        assertThat(screenings[0].movie.title).isEqualTo("탑건: 매버릭")
    }

    @Test
    fun `해당 날짜에 상영이 없으면 빈 리스트를 반환한다`() {
        val movieId = insertMovie("탑건: 매버릭")
        insertScreening(movieId, LocalDateTime.of(2026, 4, 17, 10, 0), LocalDateTime.of(2026, 4, 17, 12, 10))

        val movie = Movie("탑건: 매버릭", 130, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))
        val screenings = repository.findByMovieAndDate(movie, LocalDate.of(2026, 4, 18))

        assertThat(screenings).isEmpty()
    }

    @Test
    fun `예약된 좌석이 상영 조회 결과에 반영된다`() {
        val movieId = insertMovie("탑건: 매버릭")
        val screeningId =
            insertScreening(
                movieId,
                LocalDateTime.of(2026, 4, 17, 10, 0),
                LocalDateTime.of(2026, 4, 17, 12, 10),
            )
        val reservationId = insertReservation()
        insertReservationItem(reservationId, screeningId, "A1")

        val movie = Movie("탑건: 매버릭", 130, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))
        val screenings = repository.findByMovieAndDate(movie, LocalDate.of(2026, 4, 17))

        val reservedSeat = screenings[0].seatInventory.getSeat("A1")
        assertThat(reservedSeat.isReserved).isTrue()
    }

    private fun insertMovie(title: String): Long {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO MOVIE (title, running_time, start_date, end_date) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS,
                ).use { stmt ->
                    stmt.setString(1, title)
                    stmt.setInt(2, 130)
                    stmt.setDate(3, Date.valueOf(LocalDate.of(2026, 4, 1)))
                    stmt.setDate(4, Date.valueOf(LocalDate.of(2026, 4, 30)))
                    stmt.executeUpdate()
                    val keys = stmt.generatedKeys
                    keys.next()
                    return keys.getLong(1)
                }
        }
    }

    private fun insertScreening(
        movieId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Long {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO SCREENING (movie_id, start_time, end_time) VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS,
                ).use { stmt ->
                    stmt.setLong(1, movieId)
                    stmt.setTimestamp(2, Timestamp.valueOf(startTime))
                    stmt.setTimestamp(3, Timestamp.valueOf(endTime))
                    stmt.executeUpdate()
                    val keys = stmt.generatedKeys
                    keys.next()
                    return keys.getLong(1)
                }
        }
    }

    private fun insertReservation(): Long {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO RESERVATION (payment_method, used_point, total_price) VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS,
                ).use { stmt ->
                    stmt.setString(1, "CREDIT_CARD")
                    stmt.setInt(2, 0)
                    stmt.setInt(3, 12000)
                    stmt.executeUpdate()
                    val keys = stmt.generatedKeys
                    keys.next()
                    return keys.getLong(1)
                }
        }
    }

    private fun insertReservationItem(
        reservationId: Long,
        screeningId: Long,
        seatName: String,
    ) {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO RESERVATION_ITEM (reservation_id, screening_id, seat_name) VALUES (?, ?, ?)",
                ).use { stmt ->
                    stmt.setLong(1, reservationId)
                    stmt.setLong(2, screeningId)
                    stmt.setString(3, seatName)
                    stmt.executeUpdate()
                }
        }
    }

    private fun clearTables() {
        DatabaseConfig.getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("DELETE FROM RESERVATION_ITEM")
                stmt.execute("DELETE FROM RESERVATION")
                stmt.execute("DELETE FROM SCREENING")
                stmt.execute("DELETE FROM MOVIE")
            }
        }
    }
}
