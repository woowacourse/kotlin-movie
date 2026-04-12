@file:Suppress("NonAsciiCharacters")

package model.policy

import model.Money
import model.Screen
import model.movie.Movie
import model.movie.RunningTime
import model.screening.Screening
import model.screening.ScreeningSeatMap
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TimeDiscountPolicyTest {
    private val date = LocalDate.of(2025, 9, 15)

    private val movie =
        Movie(
            title = "테스트 영화",
            runningTime = RunningTime(120),
        )

    private val screen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.B))))

    private fun screeningAt(
        startHour: Int,
        startMinute: Int = 0,
    ) = Screening(
        movie = movie,
        startDateTime = LocalDateTime.of(date, LocalTime.of(startHour, startMinute)),
        seatMap = ScreeningSeatMap(screen),
    )

    @Test
    fun `오전 11시 이전 상영에 2000원 할인이 적용된다`() {
        val discountEffect = TimeDiscountPolicy.findDiscount(screeningAt(10))

        assertThat(discountEffect).isInstanceOf(AmountDiscount::class.java)
        discountEffect as AmountDiscount
        assertThat(discountEffect.amount).isEqualTo(Money(2_000))
    }

    @Test
    fun `오후 8시 이후 상영에 2000원 할인이 적용된다`() {
        val discountEffect = TimeDiscountPolicy.findDiscount(screeningAt(21))

        assertThat(discountEffect).isInstanceOf(AmountDiscount::class.java)
        discountEffect as AmountDiscount
        assertThat(discountEffect.amount).isEqualTo(Money(2_000))
    }

    @Test
    fun `오후 8시 정각 상영에 2000원 할인이 적용된다`() {
        val discountEffect = TimeDiscountPolicy.findDiscount(screeningAt(20))

        assertThat(discountEffect).isInstanceOf(AmountDiscount::class.java)
        discountEffect as AmountDiscount
        assertThat(discountEffect.amount).isEqualTo(Money(2_000))
    }

    @Test
    fun `해당하지 않는 시간대에는 할인이 적용되지 않는다`() {
        val discountEffect = TimeDiscountPolicy.findDiscount(screeningAt(14))

        assertThat(discountEffect).isInstanceOf(NoDiscount::class.java)
    }
}
