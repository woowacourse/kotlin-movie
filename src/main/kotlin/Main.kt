import model.CinemaKiosk
import model.CinemaTime
import model.CinemaTimeRange
import model.movie.Movie
import model.movie.MovieName
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
import java.time.LocalDateTime

fun main() {
    val screenSchedules =
        listOf(
            ScreenSchedule(
                screenId = "1",
                servicePeriod =
                    CinemaTimeRange(
                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 6, 0)),
                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 23, 0)),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName(name = "혼자사는남자", id = "1"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 10, 0)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName(name = "혼자사는남자", id = "1"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 10)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 10)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName(name = "F4 꽃보다 남자", id = "2"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 30)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 17, 30)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                    ),
            ),
            ScreenSchedule(
                screenId = "2",
                servicePeriod =
                    CinemaTimeRange(
                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 6, 0)),
                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 23, 0)),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName(name = "F4 꽃보다 남자", id = "2"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 14, 10)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 15, 10)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName(name = "혼자사는남자", id = "1"),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 15, 20)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 20)),
                                ),
                            seatGroup =
                                SeatGroup(
                                    listOf(
                                        Seat(
                                            row = SeatRow("B"),
                                            column = SeatColumn(2),
                                            state = SeatState.AVAILABLE,
                                            grade = SeatGrade.S,
                                        ),
                                        Seat(
                                            row = SeatRow("B"),
                                            column = SeatColumn(1),
                                            state = SeatState.AVAILABLE,
                                            grade = SeatGrade.B,
                                        ),
                                        Seat(
                                            row = SeatRow("A"),
                                            column = SeatColumn(2),
                                            state = SeatState.AVAILABLE,
                                            grade = SeatGrade.B,
                                        ),
                                        Seat(
                                            row = SeatRow("A"),
                                            column = SeatColumn(1),
                                            state = SeatState.AVAILABLE,
                                            grade = SeatGrade.S,
                                        ),
                                    ),
                                ),
                        ),
                    ),
            ),
        )

    val cinemaSchedule =
        CinemaSchedule(
            screenSchedules = screenSchedules,
        )
    CinemaController(cinemaKiosk = CinemaKiosk(cinemaSchedule)).run()
}
