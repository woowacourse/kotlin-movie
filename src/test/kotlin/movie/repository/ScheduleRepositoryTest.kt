package movie.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

class ScheduleRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var scheduleRepository: ScheduleRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "")
        
        val schemaSql = this::class.java.classLoader.getResource("movie.sql")?.readText()
            ?: throw IllegalStateException("movie.sql 파일을 찾을 수 없습니다.")
        
        connection.createStatement().use { stmt ->
            schemaSql.split(";").filter { it.isNotBlank() }.forEach { sql ->
                stmt.execute(sql)
            }
            
            stmt.execute("INSERT INTO movie (id, title, running_time) VALUES (1, '시동', 120)")
            stmt.execute("INSERT INTO schedule (id, movie_id, start_time, end_time) VALUES (1, 1, '2026-04-17 10:00:00', '2026-04-17 12:00:00')")
            stmt.execute("INSERT INTO reservation (id, schedule_id, total_price) VALUES (1, 1, 10000)")
            stmt.execute("INSERT INTO reserved_seat (reservation_id, schedule_id, seat_number) VALUES (1, 1, 'A1')")
        }
        
        scheduleRepository = ScheduleRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `모든 스케줄과 예약된 좌석 정보를 조회한다`() {
        val schedules = scheduleRepository.findAllSchedule()

        assertThat(schedules).hasSize(1)
        val schedule = schedules[0]
        assertThat(schedule.title).isEqualTo("시동")
        assertThat(schedule.reservedSeats).hasSize(1)
        assertThat(schedule.reservedSeats[0].seatNumber).isEqualTo("A1")
    }
}
