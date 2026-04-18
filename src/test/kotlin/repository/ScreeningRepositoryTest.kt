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

class ScreeningRepositoryTest {
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
    fun `초기 데이터의 상영 스케줄 3건을 반환한다`() {
        val schedule = ScreeningRepository(connection).getSchedule()

        assertThat(schedule.screenings).hasSize(3)
    }

    @Test
    fun `예약이 없으면 모든 좌석이 AVAILABLE 상태다`() {
        val schedule = ScreeningRepository(connection).getSchedule()

        assertThat(schedule.screenings.flatMap { it.seats.seats })
            .allMatch { it.state == ReserveState.AVAILABLE }
    }

    @Test
    fun `예약된 좌석은 RESERVED 상태로 반환된다`() {
        val screening = ScreeningRepository(connection).getSchedule().screenings.first()
        ReservationRepository(connection).save(
            TicketBucket().addTicket(screening, createSeatPositions("A" to 1, "B" to 2)),
        )

        val updatedScreening =
            ScreeningRepository(connection)
                .getSchedule()
                .screenings
                .find { it.id == screening.id }!!
        val reservedSeats = updatedScreening.seats.seats.filter { it.state == ReserveState.RESERVED }

        assertThat(reservedSeats).hasSize(2)
        assertThat(reservedSeats.map { it.position.row.name to it.position.column.value })
            .containsExactlyInAnyOrder("A" to 1, "B" to 2)
    }

    @Test
    fun `다른 상영의 예약이 다른 상영 좌석 상태에 영향을 주지 않는다`() {
        val screenings = ScreeningRepository(connection).getSchedule().screenings
        ReservationRepository(connection).save(
            TicketBucket().addTicket(screenings[0], createSeatPositions("A" to 1)),
        )

        val updated = ScreeningRepository(connection).getSchedule()
        val otherSeats =
            updated.screenings
                .filter { it.id != screenings[0].id }
                .flatMap { it.seats.seats }

        assertThat(otherSeats).allMatch { it.state == ReserveState.AVAILABLE }
    }

    companion object {
        private const val TEST_URL = "jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1"
    }
}
