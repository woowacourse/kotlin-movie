package repository

import db.DatabaseConfig
import model.cart.Cart
import model.cart.CartItem
import model.discount.PaymentMethod
import model.movie.Movie
import model.schedule.Screening
import model.seat.SeatInventory
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class ReservationRepositoryTest {
    private val repository = ReservationRepository()

    private val movie = Movie("탑건: 매버릭", 130, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))
    private val startDateTime = LocalDateTime.of(2026, 4, 17, 10, 0)
    private val screening =
        Screening(
            movie = movie,
            startDateTime = startDateTime,
            seatInventory = SeatInventory.createDefaultSeatInventory(),
        )

    @BeforeEach
    fun setUp() {
        DatabaseConfig.configure("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        DatabaseConfig.initialize()
        clearTables()
        insertMovie("탑건: 매버릭")
        insertScreening(startDateTime, startDateTime.plusMinutes(130))
    }

    @Test
    fun `예매를 저장하면 RESERVATION과 RESERVATION_ITEM이 모두 저장된다`() {
        val cart = Cart(listOf(CartItem(screening, listOf("A1", "A2"))))

        repository.save(cart, PaymentMethod.CREDIT_CARD, usedPoint = 0, totalPrice = 24000)

        assertThat(countReservations()).isEqualTo(1)
        assertThat(countReservationItems()).isEqualTo(2)
    }

    @Test
    fun `이미 예약된 좌석을 다시 예약하면 예외가 발생한다`() {
        val cart = Cart(listOf(CartItem(screening, listOf("A1"))))
        repository.save(cart, PaymentMethod.CREDIT_CARD, usedPoint = 0, totalPrice = 12000)

        val duplicateCart = Cart(listOf(CartItem(screening, listOf("A1"))))

        assertThatThrownBy {
            repository.save(duplicateCart, PaymentMethod.CREDIT_CARD, usedPoint = 0, totalPrice = 12000)
        }
    }

    @Test
    fun `예매 저장 중 예외 발생 시 트랜잭션이 롤백된다`() {
        // A1을 미리 예약해두어 두 번째 save 시 A1 삽입 시점에 UNIQUE 위반 발생
        val firstCart = Cart(listOf(CartItem(screening, listOf("A1"))))
        repository.save(firstCart, PaymentMethod.CREDIT_CARD, usedPoint = 0, totalPrice = 12000)

        // A2는 정상이지만 A1에서 실패 → 트랜잭션 전체 롤백
        val partialFailCart = Cart(listOf(CartItem(screening, listOf("A2", "A1"))))

        assertThatThrownBy {
            repository.save(partialFailCart, PaymentMethod.CREDIT_CARD, usedPoint = 0, totalPrice = 24000)
        }

        // RESERVATION은 첫 번째 예매 1건만 존재해야 한다
        assertThat(countReservations()).isEqualTo(1)
        // A2도 롤백되어 저장되지 않아야 한다
        assertThat(isReservationItemExists("A2")).isFalse()
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
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        DatabaseConfig.getConnection().use { conn ->
            conn
                .prepareStatement(
                    "INSERT INTO SCREENING (movie_id, start_time, end_time) SELECT id, ?, ? FROM MOVIE WHERE title = ?",
                ).use { stmt ->
                    stmt.setTimestamp(1, Timestamp.valueOf(startTime))
                    stmt.setTimestamp(2, Timestamp.valueOf(endTime))
                    stmt.setString(3, movie.title)
                    stmt.executeUpdate()
                }
        }
    }

    private fun countReservations(): Int {
        DatabaseConfig.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM RESERVATION")
            rs.next()
            return rs.getInt(1)
        }
    }

    private fun countReservationItems(): Int {
        DatabaseConfig.getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM RESERVATION_ITEM")
            rs.next()
            return rs.getInt(1)
        }
    }

    private fun isReservationItemExists(seatName: String): Boolean {
        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement("SELECT COUNT(*) FROM RESERVATION_ITEM WHERE seat_name = ?").use { stmt ->
                stmt.setString(1, seatName)
                val rs = stmt.executeQuery()
                rs.next()
                return rs.getInt(1) > 0
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
