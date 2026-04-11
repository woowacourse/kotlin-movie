package schedule

import io.kotest.matchers.shouldBe
import model.CinemaTime
import model.CinemaTimeRange
import model.MovieReservationResult
import model.movie.Movie
import model.movie.MovieName
import model.movie.RunningTime
import model.schedule.CinemaSchedule
import model.schedule.MovieReservationGroup
import model.schedule.MovieScreening
import model.schedule.ScreenSchedule
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatPosition
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MovieReservationGroupTest {
    @Test
    fun `이미 예약된 좌석에 예약을 시도하면 예외가 발생한다`() {
        assertThatThrownBy {
            MovieReservationGroup(
                movieReservations =
                    setOf(
                        MovieReservationResult(
                            movie =
                                Movie(
                                    name = MovieName("자취하는남자", id = "1"),
                                    runningTime = RunningTime(60),
                                ),
                            startTime =
                                CinemaTime(
                                    time = LocalDateTime.of(2026, 4, 10, 15, 30),
                                ),
                            seat =
                                Seat(
                                    SeatPosition(
                                        SeatRow("A"),
                                        SeatColumn(1),
                                    ),
                                    grade = SeatGrade.A,
                                ),
                            state = SeatState.RESERVED,
                        ),
                    ),
            ).reserve(
                cinemaSchedule =
                    CinemaSchedule(
                        screenSchedules =
                            listOf(
                                ScreenSchedule(
                                    screenId = "1",
                                    servicePeriod =
                                        CinemaTimeRange(
                                            start =
                                                CinemaTime(
                                                    LocalDateTime.of(2026, 4, 10, 15, 30),
                                                ),
                                            end =
                                                CinemaTime(
                                                    LocalDateTime.of(2027, 4, 10, 16, 30),
                                                ),
                                        ),
                                    movieScreenings =
                                        listOf(
                                            MovieScreening(
                                                movie =
                                                    Movie(
                                                        name = MovieName("자취하는남자", id = "1"),
                                                        runningTime = RunningTime(60),
                                                    ),
                                                screenTime =
                                                    CinemaTimeRange(
                                                        start =
                                                            CinemaTime(
                                                                LocalDateTime.of(2026, 4, 10, 15, 30),
                                                            ),
                                                        end =
                                                            CinemaTime(
                                                                LocalDateTime.of(2026, 4, 10, 16, 30),
                                                            ),
                                                    ),
                                                seatGroup =
                                                    SeatGroup(
                                                        listOf(
                                                            Seat(
                                                                SeatPosition(
                                                                    SeatRow("A"),
                                                                    SeatColumn(1),
                                                                ),
                                                                grade = SeatGrade.A,
                                                            ),
                                                        ),
                                                    ),
                                            ),
                                        ),
                                ),
                            ),
                    ),
                movieName = MovieName("자취하는남자", id = "1"),
                startTime =
                    CinemaTime(
                        time = LocalDateTime.of(2026, 4, 10, 15, 30),
                    ),
                seatPosition =
                    SeatPosition(
                        SeatRow("A"),
                        SeatColumn(1),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `이미 구매된 좌석에 예약을 하면 예외를 반환한다`() {
        assertThatThrownBy {
            MovieReservationGroup(
                movieReservations =
                    setOf(
                        MovieReservationResult(
                            movie =
                                Movie(
                                    name = MovieName("자취하는남자", id = "1"),
                                    runningTime = RunningTime(60),
                                ),
                            startTime =
                                CinemaTime(
                                    time = LocalDateTime.of(2026, 4, 10, 15, 30),
                                ),
                            seat =
                                Seat(
                                    SeatPosition(
                                        SeatRow("A"),
                                        SeatColumn(1),
                                    ),
                                    grade = SeatGrade.A,
                                ),
                            state = SeatState.PURCHASED,
                        ),
                    ),
            ).reserve(
                cinemaSchedule =
                    CinemaSchedule(
                        screenSchedules =
                            listOf(
                                ScreenSchedule(
                                    screenId = "1",
                                    servicePeriod =
                                        CinemaTimeRange(
                                            start =
                                                CinemaTime(
                                                    LocalDateTime.of(2026, 4, 10, 15, 30),
                                                ),
                                            end =
                                                CinemaTime(
                                                    LocalDateTime.of(2027, 4, 10, 16, 30),
                                                ),
                                        ),
                                    movieScreenings =
                                        listOf(
                                            MovieScreening(
                                                movie =
                                                    Movie(
                                                        name = MovieName("자취하는남자", id = "1"),
                                                        runningTime = RunningTime(60),
                                                    ),
                                                screenTime =
                                                    CinemaTimeRange(
                                                        start =
                                                            CinemaTime(
                                                                LocalDateTime.of(2026, 4, 10, 15, 30),
                                                            ),
                                                        end =
                                                            CinemaTime(
                                                                LocalDateTime.of(2026, 4, 10, 16, 30),
                                                            ),
                                                    ),
                                                seatGroup =
                                                    SeatGroup(
                                                        listOf(
                                                            Seat(
                                                                SeatPosition(
                                                                    SeatRow("A"),
                                                                    SeatColumn(1),
                                                                ),
                                                                grade = SeatGrade.A,
                                                            ),
                                                        ),
                                                    ),
                                            ),
                                        ),
                                ),
                            ),
                    ),
                movieName = MovieName("자취하는남자", id = "1"),
                startTime =
                    CinemaTime(
                        time = LocalDateTime.of(2026, 4, 10, 15, 30),
                    ),
                seatPosition =
                    SeatPosition(
                        SeatRow("A"),
                        SeatColumn(1),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `구매 가능한 A1 좌석을 예약하면 결과에 A1 좌석 예약 정보가 추가된다`() {
        MovieReservationGroup(
            movieReservations = emptySet(),
        ).reserve(
            cinemaSchedule =
                CinemaSchedule(
                    screenSchedules =
                        listOf(
                            ScreenSchedule(
                                screenId = "1",
                                servicePeriod =
                                    CinemaTimeRange(
                                        start =
                                            CinemaTime(
                                                LocalDateTime.of(2026, 4, 10, 15, 30),
                                            ),
                                        end =
                                            CinemaTime(
                                                LocalDateTime.of(2027, 4, 10, 16, 30),
                                            ),
                                    ),
                                movieScreenings =
                                    listOf(
                                        MovieScreening(
                                            movie =
                                                Movie(
                                                    name = MovieName("자취하는남자", id = "1"),
                                                    runningTime = RunningTime(60),
                                                ),
                                            screenTime =
                                                CinemaTimeRange(
                                                    start =
                                                        CinemaTime(
                                                            LocalDateTime.of(2026, 4, 10, 15, 30),
                                                        ),
                                                    end =
                                                        CinemaTime(
                                                            LocalDateTime.of(2026, 4, 10, 16, 30),
                                                        ),
                                                ),
                                            seatGroup =
                                                SeatGroup(
                                                    listOf(
                                                        Seat(
                                                            SeatPosition(
                                                                SeatRow("A"),
                                                                SeatColumn(1),
                                                            ),
                                                            grade = SeatGrade.A,
                                                        ),
                                                    ),
                                                ),
                                        ),
                                    ),
                            ),
                        ),
                ),
            movieName = MovieName("자취하는남자", id = "1"),
            startTime =
                CinemaTime(
                    time = LocalDateTime.of(2026, 4, 10, 15, 30),
                ),
            seatPosition =
                SeatPosition(
                    SeatRow("A"),
                    SeatColumn(1),
                ),
        ) shouldBe
            MovieReservationGroup(
                movieReservations =
                    setOf(
                        MovieReservationResult(
                            movie =
                                Movie(
                                    name = MovieName("자취하는남자", id = "1"),
                                    runningTime = RunningTime(60),
                                ),
                            startTime =
                                CinemaTime(
                                    time = LocalDateTime.of(2026, 4, 10, 15, 30),
                                ),
                            seat =
                                Seat(
                                    SeatPosition(
                                        SeatRow("A"),
                                        SeatColumn(1),
                                    ),
                                    grade = SeatGrade.A,
                                ),
                            state = SeatState.RESERVED,
                        ),
                    ),
            )
    }
}
