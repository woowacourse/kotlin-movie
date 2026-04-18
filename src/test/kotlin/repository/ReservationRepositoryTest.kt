package repository

import database.DatabaseConfig
import database.DatabaseInitializer
import domain.fixture.createSeatPositions
import domain.reservation.TicketBucket
import domain.seat.ReserveState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection

class ReservationRepositoryTest {
    private lateinit var connection: Connection

    @BeforeEach
    fun setUp() {
        DatabaseConfig.getConnection(TEST_URL).use {
            it.createStatement().use { stmt -> stmt.execute("DROP ALL OBJECTS") }
        }
        DatabaseInitializer.init(DatabaseConfig.getConnection(TEST_URL))
        connection = DatabaseConfig.getConnection(TEST_URL)
    }

    @AfterEach
    fun tearDown() {
        connection.close()
    }

    @Test
    fun `단일 티켓을 저장하면 RESERVATION 테이블에 1건이 저장된다`() {
        val screening = ScreeningRepository(connection).getSchedule().screenings.first()
        val ticketBucket = TicketBucket().addTicket(screening, createSeatPositions("A" to 1))

        ReservationRepository(connection).save(ticketBucket)

        connection.createStatement().use { stmt ->
            val rs = stmt.executeQuery("SELECT COUNT(*) FROM RESERVATION")
            rs.next()
            assertThat(rs.getInt(1)).isEqualTo(1)
        }
    }

    @Test
    fun `선택한 좌석 수만큼 RESERVATION_SEAT 테이블에 저장된다`() {
        val screening = ScreeningRepository(connection).getSchedule().screenings.first()
        val ticketBucket = TicketBucket().addTicket(screening, createSeatPositions("A" to 1, "A" to 2, "B" to 1))

        ReservationRepository(connection).save(ticketBucket)

        connection.createStatement().use { stmt ->
            val rs = stmt.executeQuery("SELECT COUNT(*) FROM RESERVATION_SEAT")
            rs.next()
            assertThat(rs.getInt(1)).isEqualTo(3)
        }
    }

    @Test
    fun `여러 티켓을 저장하면 티켓 수만큼 RESERVATION 행이 저장된다`() {
        val screenings = ScreeningRepository(connection).getSchedule().screenings
        val ticketBucket =
            TicketBucket()
                .addTicket(screenings[0], createSeatPositions("A" to 1))
                .addTicket(screenings[2], createSeatPositions("A" to 1))

        ReservationRepository(connection).save(ticketBucket)

        connection.createStatement().use { stmt ->
            val rs = stmt.executeQuery("SELECT COUNT(*) FROM RESERVATION")
            rs.next()
            assertThat(rs.getInt(1)).isEqualTo(2)
        }
    }

    @Test
    fun `저장된 예약 좌석은 조회 시 RESERVED 상태로 반환된다`() {
        val screening = ScreeningRepository(connection).getSchedule().screenings.first()
        ReservationRepository(connection).save(
            TicketBucket().addTicket(screening, createSeatPositions("A" to 1)),
        )

        val updatedScreening =
            ScreeningRepository(connection)
                .getSchedule()
                .screenings
                .find { it.id == screening.id }!!
        val seat =
            updatedScreening.seats.seats.find {
                it.position.row.name == "A" && it.position.column.value == 1
            }!!

        assertThat(seat.state).isEqualTo(ReserveState.RESERVED)
    }

    companion object {
        private const val TEST_URL = "jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1"
    }
}
