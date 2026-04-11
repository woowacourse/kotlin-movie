package domain.reservations.items

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeA
import domain.seat.items.GradeS
import domain.seat.items.RowNumber
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenTime
import domain.timetable.items.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    val reservation =
        Reservation(
            movie =
                Movie(
                    title = Title("신바드의 모험"),
                    runningTime = RunningTime(120),
                    screeningPeriod =
                        ScreeningPeriod(
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                ),
            screenTime =
                ScreenTime(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(13, 0),
                    screeningDate = LocalDate.of(2026, 4, 10),
                ),
            seats =
                Seats(
                    listOf<Seat>(
                        Seat(
                            seatPosition =
                                SeatPosition(
                                    RowNumber("A"),
                                    ColumnNumber(1),
                                ),
                            seatGrade = GradeS(),
                        ),
                        Seat(
                            seatPosition =
                                SeatPosition(
                                    RowNumber("B"),
                                    ColumnNumber(1),
                                ),
                            seatGrade = GradeA(),
                        ),
                    ),
                ),
        )

    @Test
    fun `입력된 상영 일자가 screenTime의 screeningDate과 같다면 true를 반환받는다`() {
        val result = reservation.isDuplicatedDate(LocalDate.of(2026, 4, 10))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 일자가 screenTime의 screeningDate과 같지 않으면 false를 반환받는다`() {
        val result = reservation.isDuplicatedDate(LocalDate.of(2026, 4, 11))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 상영 일자가 screenTime의 startTime과 endTime에 속한다면 true를 반환한다`() {
    }

    @Test
    fun `입력된 상영 일자가 screenTime의 startTime과 endTime에 속하지 않는다면 false를 반환한다`() {
    }
}
