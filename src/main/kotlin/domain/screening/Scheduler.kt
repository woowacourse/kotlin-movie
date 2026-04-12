package domain.screening

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.ScreeningPeriod
import domain.movie.Title
import domain.seat.Column
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object Scheduler {
    private val room1 =
        ScreeningRoom(
            name = ScreeningRoomName("1관"),
            operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(22, 0)),
            seats =
                Seats(
                    Row.entries.flatMap { row ->
                        (1..4).map { col ->
                            Seat(
                                SeatPosition(
                                    row,
                                    Column(col),
                                ),
                            )
                        }
                    },
                ),
        )

    private val room2 =
        ScreeningRoom(
            name = ScreeningRoomName("2관"),
            operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(22, 0)),
            seats = Seats(Row.entries.flatMap { row -> (1..4).map { col -> Seat(SeatPosition(row, Column(col))) } }),
        )

    private val hunnit =
        Movie(
            title = Title("허닛"),
            runningTime = RunningTime(167),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 8),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    private val Kirby =
        Movie(
            title = Title("커비"),
            runningTime = RunningTime(178),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 8),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    fun createSchedule(): ScreeningSchedule =
        ScreeningSchedule(
            screenings =
                listOf(
                    Screening(movie = hunnit, room = room1, startTime = LocalDateTime.of(2026, 4, 10, 10, 0)),
                    Screening(movie = hunnit, room = room1, startTime = LocalDateTime.of(2026, 4, 10, 14, 0)),
                    Screening(movie = hunnit, room = room1, startTime = LocalDateTime.of(2026, 4, 11, 18, 0)),
                    Screening(movie = Kirby, room = room2, startTime = LocalDateTime.of(2026, 4, 10, 10, 0)),
                    Screening(movie = Kirby, room = room2, startTime = LocalDateTime.of(2026, 4, 11, 10, 0)),
                    Screening(movie = Kirby, room = room2, startTime = LocalDateTime.of(2026, 4, 11, 14, 0)),
                ),
        )
}
