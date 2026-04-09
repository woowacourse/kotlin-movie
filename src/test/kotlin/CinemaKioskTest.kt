import model.CinemaKiosk
import model.CinemaTime
import model.CinemaTimeRange
import model.MovieReservationResult
import model.movie.Movie
import model.movie.RunningTime
import model.schedule.CinemaSchedule
import model.schedule.MovieScreening
import model.schedule.ScreenSchedule
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CinemaKioskTest {
    private lateinit var movieOne: Movie
    private lateinit var movieTwo: Movie
    private lateinit var idOne: String
    private lateinit var idTwo: String

    @BeforeEach
    fun setUp() {
        movieOne =
            Movie(
                id = "1",
                runningTime = RunningTime(60),
            )

        movieTwo =
            Movie(
                id = "2",
                runningTime = RunningTime(100),
            )

        idOne = "1"
        idTwo = "2"
    }

    @Test
    fun `동일한 시간에 예약을 했다면 예약 실패 결과를 반환한다`() {
        val screenSchedules =
            listOf(
                ScreenSchedule(
                    screenId = "1",
                    servicePeriod =
                        CinemaTimeRange(
                            CinemaTime(LocalDateTime.of(1999, 4, 7, 21, 50)),
                            CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 50)),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieOne,
                                screenTime =
                                    CinemaTimeRange(
                                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 0)),
                                    ),
                                seatGroup =
                                    SeatGroup(
                                        listOf(
                                            Seat(
                                                row = SeatRow("A"),
                                                column = SeatColumn(2),
                                                state = SeatState.AVAILABLE,
                                                grade = SeatGrade.A,
                                            ),
                                        ),
                                    ),
                            ),
                        ),
                ),
                ScreenSchedule(
                    screenId = "2",
                    servicePeriod =
                        CinemaTimeRange(
                            CinemaTime(LocalDateTime.of(1999, 4, 7, 21, 50)),
                            CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 50)),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieTwo,
                                screenTime =
                                    CinemaTimeRange(
                                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 40)),
                                    ),
                                seatGroup =
                                    SeatGroup(
                                        listOf(
                                            Seat(
                                                row = SeatRow("A"),
                                                column = SeatColumn(2),
                                                state = SeatState.AVAILABLE,
                                                grade = SeatGrade.A,
                                            ),
                                        ),
                                    ),
                            ),
                        ),
                ),
            )

        val cinemaKiosk =
            CinemaKiosk(
                cinemaSchedule =
                    CinemaSchedule(
                        screenSchedules = screenSchedules,
                    ),
            )

        cinemaKiosk.reserve(
            movie = movieOne,
            startTime = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
            seatRow = SeatRow("A"),
            seatColumn = SeatColumn(2),
        )

        assertThat(
            cinemaKiosk.reserve(
                movie = movieTwo,
                startTime = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                seatRow = SeatRow("A"),
                seatColumn = SeatColumn(2),
            ),
        ).isEqualTo(MovieReservationResult.Failed)
    }

    @Test
    fun `같은 영화 상영 일정에 두 번 이상 예약이 가능하다`() {
        val screenSchedules =
            listOf(
                ScreenSchedule(
                    screenId = idOne,
                    servicePeriod =
                        CinemaTimeRange(
                            CinemaTime(LocalDateTime.of(1999, 4, 7, 21, 50)),
                            CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 50)),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieOne,
                                screenTime =
                                    CinemaTimeRange(
                                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 0)),
                                    ),
                                seatGroup =
                                    SeatGroup(
                                        listOf(
                                            Seat(
                                                row = SeatRow("A"),
                                                column = SeatColumn(1),
                                                state = SeatState.AVAILABLE,
                                                grade = SeatGrade.A,
                                            ),
                                            Seat(
                                                row = SeatRow("A"),
                                                column = SeatColumn(2),
                                                state = SeatState.AVAILABLE,
                                                grade = SeatGrade.A,
                                            ),
                                        ),
                                    ),
                            ),
                        ),
                ),
            )

        val cinemaKiosk =
            CinemaKiosk(
                cinemaSchedule =
                    CinemaSchedule(
                        screenSchedules = screenSchedules,
                    ),
            )

        cinemaKiosk.reserve(
            movie = movieOne,
            startTime = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
            seatRow = SeatRow("A"),
            seatColumn = SeatColumn(1),
        )

        assertThat(
            cinemaKiosk.reserve(
                movie = movieOne,
                startTime = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                seatRow = SeatRow("A"),
                seatColumn = SeatColumn(2),
            ),
        ).isEqualTo(MovieReservationResult.Failed)
    }
}
