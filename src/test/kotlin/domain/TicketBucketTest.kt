package domain

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TicketBucketTest {

    @Test
    fun `티켓들을 가진다`() {
        TicketBucket(
            tickets = listOf(
                Ticket(
                    screening = Screening(
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
                    seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                )
            )
        )
    }

    @Test
    fun `티켓을 추가할 때, 상영시간이 겹치면 예외를 던진다`() {
        val bucket = TicketBucket(
            tickets = listOf(
                Ticket(
                    screening = Screening(
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
                    seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                )
            )
        )

        assertThrows(IllegalArgumentException::class.java) {
            bucket.addTicket(
                newTicket = Ticket(
                    screening = Screening(
                        movie =
                            Movie(
                                title = Title("Cㅓ비"),
                                runningTime = RunningTime(178),
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
                        startTime = LocalDateTime.of(2026, 4, 8, 10, 30),
                    ),
                    seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                )
            )
        }
    }

    @Test
    fun `티켓을 추가하면 티켓이 추가된 장바구니를 반환한다`() {
        val bucket = TicketBucket(
            tickets = listOf(
                Ticket(
                    screening = Screening(
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
                    seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                )
            )
        )

        bucket.addTicket(
            newTicket = Ticket(
                screening = Screening(
                    movie =
                        Movie(
                            title = Title("Cㅓ비"),
                            runningTime = RunningTime(178),
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
                    startTime = LocalDateTime.of(2026, 4, 8, 14, 30),
                ),
                seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
            )
        )
    }
}
