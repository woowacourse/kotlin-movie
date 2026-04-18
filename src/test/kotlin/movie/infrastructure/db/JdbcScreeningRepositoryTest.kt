package movie.infrastructure.db

import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.time.LocalTime

class JdbcScreeningRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservedSeatRepository: JdbcReservedSeatRepository
    private lateinit var screeningRepository: JdbcScreeningRepository

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()
        reservedSeatRepository = JdbcReservedSeatRepository(connection)
        screeningRepository = JdbcScreeningRepository(connection, reservedSeatRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `ID로 상영 정보를 조회할 수 있다`() {
        val screening = screeningRepository.findById(101L)

        assertThat(screening.id).isEqualTo(101L)
        assertThat(screening.titleText()).isEqualTo("F1 더 무비")
        assertThat(screening.dateText()).isEqualTo("2025-09-20")
        assertThat(screening.startTimeText()).isEqualTo("10:20")
    }

    @Test
    fun `존재하지 않는 ID로 조회하면 예외가 발생한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                screeningRepository.findById(999L)
            }
        assertThat(exception.message).isEqualTo("상영 정보를 찾을 수 없습니다.")
    }

    @Test
    fun `영화 ID로 상영 목록을 조회할 수 있다`() {
        // movie_id = 1 → F1 더 무비 (4개 상영)
        val screenings = screeningRepository.findAllByMovieId(1L)

        var count = 0
        screenings.forEachIndexed { _, _ -> count++ }
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun `조회된 상영에 예약된 좌석이 포함된다`() {
        // screening 102 = F1 더 무비 13:00, 예약 좌석 4개
        val screening = screeningRepository.findById(102L)

        val testSeat = Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B)
        assertThat(screening.isSeatAvailable(testSeat)).isFalse()
    }

    @Test
    fun `예약 좌석이 없는 상영은 모든 좌석이 사용 가능하다`() {
        // screening 101 = F1 더 무비 10:20, 예약 없음
        val screening = screeningRepository.findById(101L)

        val testSeat = Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B)
        assertThat(screening.isSeatAvailable(testSeat)).isTrue()
    }

    @Test
    fun `상영 목록은 시작 시간 순으로 정렬된다`() {
        val screenings = screeningRepository.findAllByMovieId(1L)

        val startTimes = mutableListOf<LocalTime>()
        screenings.forEachIndexed { _, screening ->
            startTimes.add(LocalTime.parse(screening.startTimeText()))
        }
        assertThat(startTimes).isSorted()
    }
}
