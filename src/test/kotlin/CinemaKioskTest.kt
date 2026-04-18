import model.CinemaKiosk
import model.CinemaTime
import model.CinemaTimeRange
import model.fixture.MovieFixture
import model.movie.RunningTime
import model.reservation.MovieReservationResult
import model.schedule.MovieScreening
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CinemaKioskTest {
    private lateinit var cinemaKiosk: CinemaKiosk
    private lateinit var seatGroup: SeatGroup

    @BeforeEach
    fun setUp() {
        cinemaKiosk = CinemaKiosk()
        seatGroup =
            SeatGroup(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.S),
                    Seat(SeatRow("A"), SeatColumn(2), SeatGrade.A),
                    Seat(SeatRow("B"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("B"), SeatColumn(2), SeatGrade.S),
                ),
            )
    }

    private fun createScreening(
        runningTimeMinutes: Int = 60,
        startHour: Int = 11,
        startMinute: Int = 0,
        seatGroup: SeatGroup = this.seatGroup,
    ): MovieScreening {
        val start = LocalDateTime.of(2026, 4, 17, startHour, startMinute)
        val end = start.plusMinutes(runningTimeMinutes.toLong())
        return MovieScreening(
            movie = MovieFixture.create(runningTime = RunningTime(runningTimeMinutes)),
            screenTime = CinemaTimeRange(CinemaTime(start), CinemaTime(end)),
            seatGroup = seatGroup,
        )
    }

    @Test
    fun `좌석 예약에 성공하면 Success를 반환한다`() {
        val screening = createScreening()
        val result = cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        assertThat(result).isInstanceOf(MovieReservationResult.Success::class.java)
    }

    @Test
    fun `예약 성공 시 reserveResults에 추가된다`() {
        val screening = createScreening()
        cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        assertThat(cinemaKiosk.reserveResults).hasSize(1)
    }

    @Test
    fun `초기 상태에서 reserveResults는 비어있다`() {
        assertThat(cinemaKiosk.reserveResults).isEmpty()
    }

    @Test
    fun `이미 예약된 좌석에 예약하면 Failed를 반환한다`() {
        val screening = createScreening()
        cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        val result = cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        assertThat(result).isEqualTo(MovieReservationResult.Failed)
    }

    @Test
    fun `시간대가 겹치는 다른 상영에 예약하면 Failed를 반환한다`() {
        val screening1 = createScreening(runningTimeMinutes = 60, startHour = 11)
        val screening2 = createScreening(runningTimeMinutes = 100, startHour = 11)
        cinemaKiosk.reserve(screening1, SeatRow("A"), SeatColumn(1))
        val result = cinemaKiosk.reserve(screening2, SeatRow("A"), SeatColumn(1))
        assertThat(result).isEqualTo(MovieReservationResult.Failed)
    }

    @Test
    fun `시간대가 겹치지 않는 다른 상영에는 예약할 수 있다`() {
        val screening1 = createScreening(runningTimeMinutes = 60, startHour = 11)
        val screening2 = createScreening(runningTimeMinutes = 60, startHour = 15)
        cinemaKiosk.reserve(screening1, SeatRow("A"), SeatColumn(1))
        val result = cinemaKiosk.reserve(screening2, SeatRow("A"), SeatColumn(1))
        assertThat(result).isInstanceOf(MovieReservationResult.Success::class.java)
    }

    @Test
    fun `여러 좌석을 한 번에 예약하면 모두 성공한다`() {
        val screening = createScreening()
        val results =
            cinemaKiosk.reserveSeats(
                movieScreening = screening,
                selectedSeats = listOf(SeatRow("A") to SeatColumn(1), SeatRow("A") to SeatColumn(2)),
            )
        assertThat(results).hasSize(2)
    }

    @Test
    fun `여러 좌석 예약 중 실패하면 이전 성공 좌석도 취소되고 예외가 발생한다`() {
        val screening = createScreening()
        cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(2))

        assertThatThrownBy {
            cinemaKiosk.reserveSeats(
                movieScreening = screening,
                selectedSeats = listOf(SeatRow("A") to SeatColumn(1), SeatRow("A") to SeatColumn(2)),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `여러 좌석 예약 실패 시 좌석을 다시 예약할 수 있다`() {
        val screening = createScreening()
        cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(2))
        runCatching {
            cinemaKiosk.reserveSeats(
                movieScreening = screening,
                selectedSeats = listOf(SeatRow("A") to SeatColumn(1), SeatRow("A") to SeatColumn(2)),
            )
        }
        val result = cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        assertThat(result).isInstanceOf(MovieReservationResult.Success::class.java)
    }

    @Test
    fun `취소 후 해당 좌석을 다시 예약할 수 있다`() {
        val screening = createScreening()
        cinemaKiosk.reserve(screening, SeatRow("A"), SeatColumn(1))
        cinemaKiosk.cancelReservations(screening, listOf(SeatRow("A") to SeatColumn(1)))
        val result = screening.reserve(SeatRow("A"), SeatColumn(1))
        assertThat(result).isInstanceOf(MovieReservationResult.Success::class.java)
    }
}
