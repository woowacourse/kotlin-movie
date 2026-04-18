package movie.service

import movie.repository.ScheduleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

class ScheduleServiceTest {
    private lateinit var connection: Connection
    private lateinit var scheduleService: ScheduleService

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
        }
        
        val scheduleRepository = ScheduleRepository(connection)
        scheduleService = ScheduleService(scheduleRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `DB에서 스케줄을 가져와 도메인 모델(Schedules)로 변환한다`() {
        val schedules = scheduleService.getSchedules()

        assertThat(schedules.getMovieTitles().map { it.value }).contains("시동")
        val movieSchedules = schedules.getMovieSchedules(movie.domain.MovieTitle("시동"), java.time.LocalDate.of(2026, 4, 17))
        assertThat(movieSchedules).hasSize(1)
    }
}
