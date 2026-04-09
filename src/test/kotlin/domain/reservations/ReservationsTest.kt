package domain.reservations

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeA
import domain.seat.items.GradeS
import domain.seat.items.RowNumber
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `reservation 목록 중 입력받은 screenTime과 겹치는 reservation이 있다면 true를 반환받는다`() {
        val result = reservations.checkDuplicateTime(LocalTime.of(12, 0))

        assertThat(result).isTrue()
    }

    @Test
    fun `reservation 목록 중 입력받은 screenTime과 겹치는 reservation이 없다면 false를 반환받는다`() {
        val result = reservations.checkDuplicateTime(LocalTime.of(14, 0))

        assertThat(result).isFalse()
    }

    @Test
    fun `reservation 목록 중 입력받은 screenDate와 겹치는 reservation이 있다면 true를 반환받는다`() {
        val result = reservations.checkDuplicateDate(LocalDate.of(2026, 4, 10))

        assertThat(result).isTrue()
    }

    @Test
    fun `reservation 목록 중 입력받은 screenDate와 겹치는 reservation이 없다면 false를 반환받는다`() {
        val result = reservations.checkDuplicateDate(LocalDate.of(2026, 4, 11))

        assertThat(result).isFalse()
    }

    companion object {
        val reservations = Reservations()

        @BeforeAll
        @JvmStatic
        fun setUpReservation() {
            reservations.addReservation(
                Reservation(
                    movie =
                        Movie(
                            title = Title("신바드의 모험"),
                            runningTime = RunningTime(120),
                            screeningPeriod =
                                ScreeningPeriod(
                                    startDate =
                                        LocalDate.of(
                                            2026,
                                            4,
                                            1,
                                        ),
                                    endDate =
                                        LocalDate.of(
                                            2026,
                                            4,
                                            30,
                                        ),
                                ),
                        ),
                    screenTime =
                        ScreenTime(
                            startTime =
                                LocalTime.of(
                                    11,
                                    0,
                                ),
                            endTime =
                                LocalTime.of(
                                    13,
                                    0,
                                ),
                            screeningDate =
                                LocalDate.of(
                                    2026,
                                    4,
                                    10,
                                ),
                        ),
                    seats =
                        listOf<Seat>(
                            Seat(
                                rowNumber = RowNumber("A"),
                                columnNumber = ColumnNumber(1),
                                seatGrade = GradeS(),
                            ),
                            Seat(
                                rowNumber = RowNumber("B"),
                                columnNumber = ColumnNumber(1),
                                seatGrade = GradeA(),
                            ),
                        ),
                ),
            )
        }
    }
}
