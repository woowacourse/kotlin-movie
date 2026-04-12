import controller.Controller
import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.ScreeningPeriod
import domain.movie.Title
import domain.screening.Screening
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.screening.ScreeningSchedule
import domain.screening.TimeRange
import domain.seat.Column
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun main() {
    Controller(
        ScreeningSchedule(
            screenings =
                listOf(
                    Screening(
                        movie =
                            Movie(
                                title = Title("허닛"),
                                runningTime = RunningTime(167),
                                screeningPeriod =
                                    ScreeningPeriod(
                                        startDate = LocalDate.of(2026, 4, 8),
                                        endDate = LocalDate.of(2026, 4, 9),
                                    ),
                            ),
                        room =
                            ScreeningRoom(
                                name = ScreeningRoomName("커피"),
                                operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                                seats =
                                    Seats(
                                        listOf(
                                            Seat(
                                                position =
                                                    SeatPosition(
                                                        Row("A"),
                                                        Column(1),
                                                    ),
                                            ),
                                        ),
                                    ),
                            ),
                        startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                    ),
                ),
        ),
    ).run()
}
