import controller.Controller
import domain.Column
import domain.Movie
import domain.Row
import domain.RunningTime
import domain.Screening
import domain.ScreeningPeriod
import domain.ScreeningRoom
import domain.ScreeningRoomName
import domain.ScreeningSchedule
import domain.Seat
import domain.SeatPosition
import domain.Seats
import domain.TimeRange
import domain.Title
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
