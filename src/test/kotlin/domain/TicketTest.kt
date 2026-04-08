package domain

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TicketTest {
    @Test
    fun `상영 정보와 예매 좌석을 가진다`() {
        Ticket(
            screening =
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
                            seats = Seats(listOf(Seat(position = SeatPosition(Row("A"), Column(1))))),
                        ),
                    startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                ),
            seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
        )
    }

    @Test
    fun `예매 좌석에 중복이 있을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Ticket(
                screening =
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
                seatPositions =
                    SeatPositions(
                        listOf(
                            SeatPosition(Row("A"), Column(1)),
                            SeatPosition(Row("A"), Column(1)),
                        ),
                    ),
            )
        }
    }
}
