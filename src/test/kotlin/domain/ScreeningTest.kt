package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningTest {
    @Test
    fun `영화, 상영관, 상영 날짜 및 시간을 가진다`() {
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
        )
    }

    @Test
    fun `상영 시작 시간과 영화 상영 길이에 따른 시작 및 종료 시간을 가진다`() {
        val screening =
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
            )
        assertThat(screening.screenTimeRange.start).isEqualTo(LocalTime.of(10, 0))
        assertThat(screening.screenTimeRange.end).isEqualTo(LocalTime.of(12, 47))
    }

    @Test
    fun `상영 시간이 상영관의 운영시간에 포함되지 않을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
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
                startTime = LocalDateTime.of(2026, 4, 8, 9, 0),
            )
        }
    }

    @Test
    fun `좌석을 예약 시 좌석의 상태가 바뀐다`() {
        val screening =
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
            )

        screening.reserve(position = SeatPosition(Row("A"), Column(1)))
    }
}
