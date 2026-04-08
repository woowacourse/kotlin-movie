import model.CinemaSchedule
import model.DateTimeRange
import model.Movie
import model.MovieReservationResult
import model.MovieScreening
import model.RunningTime
import model.ScreenSchedule
import model.SeatGroup
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CinemaKioskTest {
    @Test
    fun `동일한 시간에 예약을 했다면 예약 실패 결과를 반환한다`() {
        val movieOne =
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(60),
            )
        val movieTwo =
            Movie(
                id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                runningTime = RunningTime(100),
            )

        val screenSchedules =
            listOf(
                ScreenSchedule(
                    screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                    servicePeriod =
                        DateTimeRange(
                            LocalDateTime.of(1999, 4, 7, 21, 50),
                            LocalDateTime.of(2026, 4, 10, 22, 50),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieOne,
                                screenTime =
                                    DateTimeRange(
                                        start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                        end = LocalDateTime.of(2026, 4, 8, 12, 0),
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
                    screenId = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                    servicePeriod =
                        DateTimeRange(
                            LocalDateTime.of(1999, 4, 7, 21, 50),
                            LocalDateTime.of(2026, 4, 10, 22, 50),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieTwo,
                                screenTime =
                                    DateTimeRange(
                                        start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                        end = LocalDateTime.of(2026, 4, 8, 12, 40),
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
            startTime = LocalDateTime.of(2026, 4, 8, 11, 0),
            seatRow = SeatRow("A"),
            seatColumn = SeatColumn(2),
        )

        assertThat(
            cinemaKiosk.reserve(
                movie = movieTwo,
                startTime = LocalDateTime.of(2026, 4, 8, 11, 0),
                seatRow = SeatRow("A"),
                seatColumn = SeatColumn(2),
            ),
        ).isEqualTo(MovieReservationResult.Failed)
    }

    @Test
    fun `같은 영화 상영 일정에 두 번 이상 예약이 가능하다`() {
        val movieOne =
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(60),
            )

        val screenSchedules =
            listOf(
                ScreenSchedule(
                    screenId = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                    servicePeriod =
                        DateTimeRange(
                            LocalDateTime.of(1999, 4, 7, 21, 50),
                            LocalDateTime.of(2026, 4, 10, 22, 50),
                        ),
                    movieScreenings =
                        listOf(
                            MovieScreening(
                                movie = movieOne,
                                screenTime =
                                    DateTimeRange(
                                        start = LocalDateTime.of(2026, 4, 8, 11, 0),
                                        end = LocalDateTime.of(2026, 4, 8, 12, 0),
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
            startTime = LocalDateTime.of(2026, 4, 8, 11, 0),
            seatRow = SeatRow("A"),
            seatColumn = SeatColumn(1),
        )

        assertThat(
            cinemaKiosk.reserve(
                movie = movieOne,
                startTime = LocalDateTime.of(2026, 4, 8, 11, 0),
                seatRow = SeatRow("A"),
                seatColumn = SeatColumn(2),
            ),
        ).isEqualTo(MovieReservationResult.Failed)
    }
}
