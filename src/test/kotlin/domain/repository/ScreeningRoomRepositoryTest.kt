package domain.repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.JdbcConnection
import repository.ScreeningRoomRepository
import repository.SimpleDataSource
import java.sql.Connection
import java.sql.Time
import java.time.LocalTime

class ScreeningRoomRepositoryTest {

    private lateinit var connection: Connection
    private lateinit var roomRepository: ScreeningRoomRepository

    @BeforeEach
    fun setUp() {
        connection = JdbcConnection.getConnection()
        roomRepository = ScreeningRoomRepository(SimpleDataSource())

        connection.createStatement().use { stmt ->
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS screening_rooms (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    operating_start_time TIME NOT NULL,
                    operating_end_time TIME NOT NULL
                )
            """.trimIndent())

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE")
            stmt.execute("TRUNCATE TABLE screening_rooms RESTART IDENTITY")
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE")
        }
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS screening_rooms CASCADE")
        }
        connection.close()
    }

    @Test
    fun `findById는 존재하는 상영관 ID로 조회 시 상영관 객체를 반환한다`() {
        // given
        val name = "1관"
        val start = LocalTime.of(9, 0)
        val end = LocalTime.of(22, 0)

        insertRoom(name, start, end)

        // when
        val room = roomRepository.findById(1L)

        // then
        room shouldNotBe null
        room.name.value shouldBe name
        room.operatingTime.start shouldBe start
        room.operatingTime.end shouldBe end
    }

    @Test
    fun `findById는 존재하지 않는 ID로 조회 시 예외를 던진다`() {
        // given
        val invalidId = -1L

        // when & then
        shouldThrow<IllegalArgumentException> {
            roomRepository.findById(invalidId)
        }.message shouldBe "상영관을 찾을 수 없습니다."
    }

    private fun insertRoom(name: String, start: LocalTime, end: LocalTime) {
        val sql = "INSERT INTO screening_rooms (name, operating_start_time, operating_end_time) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { stmt ->
            stmt.setString(1, name)
            stmt.setTime(2, Time.valueOf(start))
            stmt.setTime(3, Time.valueOf(end))
            stmt.executeUpdate()
        }
    }
}
