package domain.discountpolicy

import com.github.ajalt.mordant.widgets.progress.progressBarLayout
import domain.money.Money
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
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountPolicyTest {
    val timeDiscountPolicy = TimeDiscountPolicy()
    val movieDayDiscountPolicy = MovieDayDiscountPolicy()

    @Test
    fun `예매의 시간이 11시 이전이면 2000원 할인된다` () {
        val amount = Money(10000)

        val reservation = Reservation(
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
        )

        val result = timeDiscountPolicy.applyDiscount(amount, reservation)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 8시 이후면 2000원 할인된다` () {
        val amount = Money(10000)

        val reservation = Reservation(
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
                            20,
                            0,
                        ),
                    endTime =
                        LocalTime.of(
                            22,
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
        )

        val result = timeDiscountPolicy.applyDiscount(amount, reservation)

        assertThat(result).isEqualTo(Money(8000))
    }

    @Test
    fun `예매의 시간이 11시부터 20시 사이면 할인이 적용되지 않는다` () {
        val amount = Money(10000)

        val reservation = Reservation(
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
                            15,
                            0,
                        ),
                    endTime =
                        LocalTime.of(
                            17,
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
        )

        val result = timeDiscountPolicy.applyDiscount(amount, reservation)

        assertThat(result).isEqualTo(Money(10000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나면 10% 할인된다` () {
        val amount = Money(10000)

        val reservation = Reservation(
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
        )

        val result = movieDayDiscountPolicy.applyDiscount(amount, reservation)

        assertThat(result).isEqualTo(Money(9000))
    }

    @Test
    fun `예매의 일자가 10일, 20일, 30일 중 하나가 아니면 할인되지 않는다` () {
        val amount = Money(10000)

        val reservation = Reservation(
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
                            15,
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
        )

        val result = movieDayDiscountPolicy.applyDiscount(amount, reservation)

        assertThat(result).isEqualTo(Money(10000))
    }
}