package domain.discountpolicy

import domain.money.Money
import domain.reservations.items.ReservationInfo
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class DiscountConditionTest {
    val timeCondition = TimeCondition()
    val movieDayCondition = MovieDayCondition()

    @Test
    fun `입력된 일자가 10, 20, 30일 중 하나면 true를 반환한다`() {
        val info =
            ReservationInfo(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
                price = Money(12000),
            )

        assertThat(movieDayCondition.isSatisfiedBy(info)).isTrue()
    }

    @Test
    fun `입력된 일자가 10, 20, 30일 이 아니면 false를 반환한다`() {
        val info =
            ReservationInfo(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
                price = Money(12000),
            )

        assertThat(movieDayCondition.isSatisfiedBy(info)).isFalse()
    }

    @Test
    fun `입력된 시간이 11시 이전이면 true를 반환한다`() {
        val info =
            ReservationInfo(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(12, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
                price = Money(12000),
            )

        assertThat(timeCondition.isSatisfiedBy(info)).isTrue()
    }

    @Test
    fun `입력된 시간이 20시 이후면 true를 반환한다`() {
        val info =
            ReservationInfo(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(20, 0),
                        endTime = LocalTime.of(22, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
                price = Money(12000),
            )

        assertThat(timeCondition.isSatisfiedBy(info)).isTrue()
    }

    @Test
    fun `입력된 시간이 11이후 20시 이전이면 false를 반환한다`() {
        val info =
            ReservationInfo(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(13, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 15),
                    ),
                price = Money(12000),
            )

        assertThat(timeCondition.isSatisfiedBy(info)).isFalse()
    }
}
