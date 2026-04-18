package domain.repository

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.JdbcConnection
import repository.ScreeningRepository
import repository.SimpleDataSource
import java.sql.Connection
import java.sql.Timestamp

class ScreeningRepositoryTest {

    private lateinit var connection: Connection
    private lateinit var screeningRepository: ScreeningRepository

    @BeforeEach
    fun setUp() {
        connection = JdbcConnection.getConnection()
        screeningRepository = ScreeningRepository(SimpleDataSource())

        connection.createStatement().use { stmt ->
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), running_time INT, start_date DATE, end_date DATE)")
            stmt.execute("CREATE TABLE IF NOT EXISTS screening_rooms (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), operating_start_time TIME, operating_end_time TIME)")
            stmt.execute("CREATE TABLE IF NOT EXISTS screenings (id BIGINT AUTO_INCREMENT PRIMARY KEY, movie_id BIGINT, room_id BIGINT, start_time TIMESTAMP)")
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (id BIGINT AUTO_INCREMENT PRIMARY KEY, used_points INT, payment_method VARCHAR(50), total_price INT)")
            stmt.execute("CREATE TABLE IF NOT EXISTS reserved_seats (id BIGINT AUTO_INCREMENT PRIMARY KEY, reservation_id BIGINT NOT NULL, screening_id BIGINT NOT NULL, seat_row VARCHAR(5), seat_column INT)")

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE")
            stmt.execute("TRUNCATE TABLE reserved_seats RESTART IDENTITY")
            stmt.execute("TRUNCATE TABLE reservations RESTART IDENTITY")
            stmt.execute("TRUNCATE TABLE screenings RESTART IDENTITY")
            stmt.execute("TRUNCATE TABLE screening_rooms RESTART IDENTITY")
            stmt.execute("TRUNCATE TABLE movies RESTART IDENTITY")
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE")
        }
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS reserved_seats CASCADE")
            stmt.execute("DROP TABLE IF EXISTS screenings CASCADE")
            stmt.execute("DROP TABLE IF EXISTS screening_rooms CASCADE")
            stmt.execute("DROP TABLE IF EXISTS movies CASCADE")
        }
        connection.close()
    }

    @Test
    fun `findAll은 모든 상영 정보와 예약된 좌석 상태를 반환한다`() {
        // given
        setupTestData()

        // when
        val screenings = screeningRepository.findAll()

        // then
        screenings shouldHaveSize 1
        val screening = screenings[0]
        
        screening.movie.title.title shouldBe "허닛"
        screening.room.name.value shouldBe "커피"
        
        val seatA1 = screening.seats.seats.find {
            it.position.row.value == "A" && it.position.column.value == 1 
        }
        seatA1 shouldNotBe null
        seatA1?.isReservable() shouldBe false
    }

    private fun setupTestData() {
        connection.prepareStatement("INSERT INTO movies VALUES (1, '허닛', 167, '2026-04-08', '2026-04-09')").use { it.executeUpdate() }

        connection.prepareStatement("INSERT INTO screening_rooms VALUES (1, '커피', '10:00:00', '18:00:00')").use { it.executeUpdate() }

        val startTime = Timestamp.valueOf("2026-04-10 10:00:00")
        connection.prepareStatement("INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (1, 1, 1, ?)").use {
            it.setTimestamp(1, startTime)
            it.executeUpdate()
        }

        connection.prepareStatement("INSERT INTO reservations (id, used_points, payment_method, total_price) VALUES (1, 0, 'CASH', 0)").use {
            it.executeUpdate()
        }

        connection.prepareStatement("INSERT INTO reserved_seats (reservation_id, screening_id, seat_row, seat_column) VALUES (1, 1, 'A', 1)").use {
            it.executeUpdate()
        }
    }
}
