package domain

import domain.model.Column
import domain.model.Movie
import domain.model.Row
import domain.model.RunningTime
import domain.model.Screening
import domain.model.ScreeningPeriod
import domain.model.ScreeningRoom
import domain.model.ScreeningRoomName
import domain.model.Seat
import domain.model.SeatPosition
import domain.model.Seats
import domain.model.TimeRange
import domain.model.Title
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningScheduleTest {

    @Test
    fun `상영 정보 리스트를 가진다`() {
        ScreeningSchedule(
            screenings = listOf(
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
                            seats = Seats(
                                listOf(
                                    Seat(
                                        position = SeatPosition(
                                            Row("A"),
                                            Column(1)
                                        )
                                    )
                                )
                            ),
                        ),
                    startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                )
            )
        )
    }

    @Test
    fun `한 영화가 동시에 상영될 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningSchedule(
                screenings = listOf(
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
                                seats = Seats(
                                    listOf(
                                        Seat(
                                            position = SeatPosition(
                                                Row("A"),
                                                Column(1)
                                            )
                                        )
                                    )
                                ),
                            ),
                        startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                    ),
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
                                name = ScreeningRoomName("커피샵"),
                                operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                                seats = Seats(
                                    listOf(
                                        Seat(
                                            position = SeatPosition(
                                                Row("A"),
                                                Column(1)
                                            )
                                        )
                                    )
                                ),
                            ),
                        startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                    )
                )
            )
        }

    }

}
